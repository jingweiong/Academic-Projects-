#include <stdio.h>
#include <math.h>
#include <string.h>
#include <stdlib.h>
#include <time.h>
#include <unistd.h>
#include <pthread.h>
#include <stdbool.h>
#include <mpi.h>
#include <omp.h>

#define NBRILO 10
#define NBRIHI 11
#define NBRJLO 12
#define NBRJHI 13
#define SHIFT_ROW 0
#define SHIFT_COL 1
#define DISP 1

#define NUM_CHARGING_PORTS 3
#define NUM_STATION 9
#define ITERATION_INTERVAL 2
#define NUM_ITERATIONS 3
#define ARRAY_SIZE 5

typedef struct
{
    int port_number;
    int availability;
    pthread_mutex_t mutex;
    pthread_t tid;

} ChargingPort;

ChargingPort ports[NUM_CHARGING_PORTS];

typedef struct {
    int time;          // Time (or iteration) when data is recorded
    int availability;  // Total available ports
} ChargingNodeData;

typedef struct {
    ChargingNodeData data[ARRAY_SIZE]; // Circular buffer for data
    int front;           // Index of the front element
    int rear;            // Index of the rear element
    int count;           // Number of elements in the buffer
    pthread_mutex_t mutex;
} CircularArray;

CircularArray stationData[NUM_STATION];


typedef struct {
    int reporting_node;
    time_t time;
    int coordx;
    int coordy;
    int num_port;
    int port_available;
    int neighbours[4];
    int neighbours_availability[4];
} NodeReport;

int getTotalAvailability();
void *simulateChargingPort(void* arg);
void initSharedArray(CircularArray* array);
void addToArray(CircularArray* array, ChargingNodeData item);
void printBuffer(CircularArray* buffer, FILE *file); 

int master_io(MPI_Comm master_comm, MPI_Comm comm); 
int slave_io(MPI_Comm master_comm, MPI_Comm comm);
int nrows;
int ncols;


int main(int argc, char *argv[]) {
	
	/* start up initial MPI environment */
    int ndims=2, size, my_rank;
    int dims[ndims],coord[ndims];
    int wrap_around[ndims];
    MPI_Comm new_comm;
	MPI_Init(NULL, NULL);
	MPI_Comm_size(MPI_COMM_WORLD, &size);
	MPI_Comm_rank(MPI_COMM_WORLD, &my_rank);

	
	/* process command line arguments*/
	if (argc == 3) {
		nrows = atoi (argv[1]);
		ncols = atoi (argv[2]);
		dims[0] = nrows; /* number of rows */
		dims[1] = ncols; /* number of columns */
		if( (nrows*ncols)+1 != size) {
			if( my_rank ==0) printf("ERROR: nrows*ncols)=%d * %d = %d != %d \nPlease add one more processor for base station\n", nrows, ncols, nrows*ncols,size);
			MPI_Finalize(); 
			return 0;
		}
	} else {
		nrows=ncols=(int)sqrt(size);
		dims[0]=dims[1]=0;

	}

    if(my_rank==0)
		printf("PW[%d], CommSz[%d]: PEdims = [%d x %d] \n",my_rank,size,dims[0],dims[1]);

    MPI_Comm_split( MPI_COMM_WORLD,my_rank == 0, 0, &new_comm);
    if (my_rank == 0){
        master_io( MPI_COMM_WORLD, new_comm ); 
    }
    else{
        slave_io( MPI_COMM_WORLD, new_comm );
    }

	MPI_Finalize();
	return 0;
}

/* This is the master */
int master_io(MPI_Comm master_comm, MPI_Comm comm)
{
    FILE *mFile;
    char buf[256] = {0};
    sprintf(buf, "BaseStation_log.txt");
    mFile = fopen(buf, "w");
    int size, nslave;
    time_t t;   // not a primitive datatype
    time(&t);
    MPI_Comm_size( master_comm, &size );
    MPI_Request send_request[size];
    MPI_Status status;
    int neighbour_coords[4][2];  // To store the coordinates of neighbors
    int iter = 0;
    
    
    // Base station logic
    
    for (int iteration = 0; iteration < NUM_ITERATIONS; iteration++) {
        // Perform tasks in each iteration
        printf("Iteration %d: Base station is running...\n", iteration + 1);
        int unavailable[size];

        NodeReport *node_report = (NodeReport*) malloc(sizeof(NodeReport) * (size-1));
        for (int i = 1; i < size; i++)
        {
            MPI_Recv( &node_report[i-1], sizeof(NodeReport), MPI_BYTE, i, 0, master_comm, &status);
        }

        for (int i = 0; i < size-1; i++)
        {
            int reporting_node = node_report[i].reporting_node;
            if (reporting_node != -1)
            {
                unavailable[i] = reporting_node;
                int top = (node_report[i].coordx > 0) ? reporting_node - ncols : -100;
                int bottom = (node_report[i].coordx < nrows - 1) ? reporting_node + ncols : -100;
                int left = (node_report[i].coordy > 0) ? reporting_node - 1 : -100;
                int right = (node_report[i].coordy < ncols - 1) ? reporting_node + 1 : -100;

                int nearby_nodes[8] = {
                    top-ncols, top-1, top+1, left-1, right+1, bottom-1, bottom+1, bottom+ncols
                };

                int available_nearby_nodes;

                fprintf(mFile, "\n----------------------------------------------------------------------------\n");
                fprintf(mFile, "Iteration           : %d\n", iter);
                iter++;
                fprintf(mFile, "Logged time         : %s\n", ctime(&t));
                fprintf(mFile, "Alert reported time : %s\n", ctime(&node_report[i].time));
                fprintf(mFile, "Reporting Node     Coord     Port Value     Available Port\n");
                fprintf(mFile, "%d                  (%d,%d)     %d              %d\n", reporting_node, node_report[i].coordx, node_report[i].coordy, node_report[i].num_port, node_report[i].port_available);
                fprintf(mFile, "\n");
                fprintf(mFile, "Adjacent Node      Coord     Port Value     Available Port\n");
                for (int j = 0; j < 4; j++)
                {
                    printf("Neighbours of %d: %d\n",reporting_node, node_report[i].neighbours[j]);
                    if (node_report[i].neighbours[j] != -2)
                    {
                        //To compute 2D coordinates (i, j) from a 1D rank index 
                        neighbour_coords[j][0] = j / ncols;
                        neighbour_coords[j][1] = j % ncols;
                        fprintf(mFile, "%d                  (%d,%d)     %d              %d\n", node_report[i].neighbours[j], neighbour_coords[j][0], neighbour_coords[j][1], NUM_CHARGING_PORTS, node_report[i].neighbours_availability[j]);
                    }
                }
                fprintf(mFile, "\n");
                fprintf(mFile, "Nearby Nodes       Coord\n");
                for (int k = 0; k < 8; k++)
                {
                    int cx = nearby_nodes[k] / ncols;
                    int cy = nearby_nodes[k] % ncols;
                    if (cx >= 0 && cx < nrows && cy >= 0 && cy < ncols) 
                    {
                        fprintf(mFile, "%d                   (%d,%d)\n", nearby_nodes[k], cx, cy);
                        for (int n = 0; n < size; n++)
                        {
                            if (nearby_nodes[k] != unavailable[n])
                            {
                                available_nearby_nodes = nearby_nodes[k];
                            }
                        }
                    }
                }
                MPI_Request req;
                MPI_Isend(&available_nearby_nodes, 1, MPI_INT, reporting_node, 0, MPI_COMM_WORLD, &req);
            }
        }
        
        // Sleep for ITERATION_INTERVAL seconds before the next iteration
        sleep(ITERATION_INTERVAL);
    }
    // Send termination signal to charging nodes
    int termination_signal = 1;
    for (int node_rank = 1; node_rank < size; node_rank++) {
        MPI_Isend(&termination_signal, 1, MPI_INT, node_rank, 0, MPI_COMM_WORLD, &send_request[node_rank-1]);
    }

    MPI_Waitall(size-1, send_request, MPI_STATUSES_IGNORE);

    return 0;
}

/* This is the slave */
int slave_io(MPI_Comm master_comm, MPI_Comm comm2D)
{
    int my_rank, size;
    FILE *sFile;
    MPI_Comm_rank( comm2D, &my_rank );
    MPI_Comm_size( comm2D, &size);

    /*************************************************************/
	/* create cartesian topology for processes */
	/*************************************************************/
    int ndims = 2, reorder, ierr, nbr_i_lo, nbr_i_hi, nbr_j_lo, nbr_j_hi, my_cart_rank;
    int dims[ndims];
    dims[0] = nrows;
    dims[1] = ncols;
    int coord[ndims];
    int wrap_around[ndims];
    char buf[256] = {0};
	MPI_Dims_create(size, ndims, dims);

        MPI_Request send_request[4];
        MPI_Request receive_request[4];
        MPI_Status send_status[4];
        MPI_Status receive_status[4];
	
	/* create cartesian mapping */
	wrap_around[0] = wrap_around[1] = 0; /* periodic shift is .false. */
	reorder = 1;
	ierr = 0;
    MPI_Comm cart_comm;
	ierr = MPI_Cart_create(comm2D, ndims, dims, wrap_around, reorder, &cart_comm);
	if(ierr != 0) printf("ERROR[%d] creating CART\n",ierr);

	
	/* find my coordinates in the cartesian communicator group */
	MPI_Cart_coords(cart_comm, my_rank, ndims, coord);
	/* use my cartesian coordinates to find my rank in cartesian group*/
	MPI_Cart_rank(cart_comm, coord, &my_cart_rank);
	/* get my neighbors; axis is coordinate dimension of shift */
	/* axis=0 ==> shift along the rows: P[my_row-1]: P[me] : P[my_row+1] */
	/* axis=1 ==> shift along the columns P[my_col-1]: P[me] : P[my_col+1] */
	
	MPI_Cart_shift( cart_comm, SHIFT_ROW, DISP, &nbr_i_lo, &nbr_i_hi );
	MPI_Cart_shift( cart_comm, SHIFT_COL, DISP, &nbr_j_lo, &nbr_j_hi );
	int action_list[4] = {nbr_j_lo, nbr_j_hi, nbr_i_lo, nbr_i_hi};
    int recv_data[4] = {-1, -1, -1, -1};

    for (int i = 0; i < size; i++) {
        initSharedArray(&stationData[i]);
    }

    sprintf(buf, "Station_%d.txt", my_rank);
	sFile = fopen(buf, "w");
	
	fprintf(sFile, "Global rank: %d. Cart rank: %d. Coord: (%d, %d). Left: %d. Right: %d. Top: %d. Bottom: %d\n\n", my_rank, my_cart_rank, coord[0], coord[1], nbr_j_lo, nbr_j_hi, nbr_i_lo, nbr_i_hi);

     // Charging node logic
    int termination_signal;
    MPI_Request requests;
    MPI_Irecv(&termination_signal, 1, MPI_INT, 0, 0, MPI_COMM_WORLD, &requests);

    int terminated = 0;

    while (!terminated)
    {
        for (int i = 0; i < NUM_CHARGING_PORTS; i++) {
            ports[i].port_number = i;
            ports[i].availability = 1; // Initially all ports are available
            pthread_mutex_init(&ports[i].mutex, NULL);
            pthread_create(&ports[i].tid, NULL, simulateChargingPort, &ports[i]);
        }        
        
        int totalAvailability = getTotalAvailability();
        printf("EV charging node: %d Total Availability: %d\n", my_rank, totalAvailability);   
        ChargingNodeData data;
        data.time = time(NULL);
        data.availability =  totalAvailability;
        addToArray(&stationData[my_rank], data);
        
        fprintf(sFile, "Time: %d, Availability: %d\n", data.time, data.availability);
 
        if (totalAvailability == 0)
        {
            int request_flag = 1;
            printf("All ports in EV charging node %d are in FULL use!\n", my_rank);
            fprintf(sFile, "\nAll ports are in FULL use!\n");
            fprintf(sFile, "Getting nearest available neighbour nodes' data...\n");
            for (int i = 0; i < 4; i++)
            {
                MPI_Isend( &request_flag, 1, MPI_INT, action_list[i], 0, cart_comm, &send_request[i]);
                MPI_Irecv( &recv_data[i], 1, MPI_INT, action_list[i], 0, cart_comm, &receive_request[i]);
            }
            MPI_Waitall(4, send_request, send_status);
    

            
            for (int i = 0; i < 4; i++)
            {
                printf("%d\n", recv_data[i]);
                if (recv_data[i] != -1)
                {
                    fprintf(sFile, "   Neighbour Node %d; Availability: %d\n", action_list[i], recv_data[i]);
                }
            }
            
            fprintf(sFile, "\n");
            
        }

        int flag, recv_rank;
        MPI_Status status;
        MPI_Request recv_reqq[4];
        MPI_Request send_reqq;
        MPI_Status recv_status[4];
        MPI_Status send_stat[4];

        for (int i = 0; i < 4; i++)
        {
            MPI_Iprobe(action_list[i], 0, cart_comm, &flag, &status);
            if (flag)
            {
                MPI_Irecv(&recv_rank, 1, MPI_INT, action_list[i], 0, cart_comm, &recv_reqq[i]);
            }
        }
        if (flag)
        {
            MPI_Isend(&totalAvailability, 1, MPI_INT, recv_rank, 0, cart_comm, &send_reqq);
        }
        

        time_t reporttime;
        NodeReport report;
        report.reporting_node = my_rank;
        report.time = time(&reporttime);
        report.coordx = coord[0];
        report.coordy = coord[1];
        report.num_port = NUM_CHARGING_PORTS;
        report.port_available = totalAvailability;
        for (int i = 0; i < 4; i++)
        {
            report.neighbours[i] = action_list[i];
            report.neighbours_availability[i] = recv_data[i];
        }

        int nearby_node;
        MPI_Request mas_req;
        if (totalAvailability == 0)
        {
            MPI_Send( &report, sizeof(NodeReport), MPI_BYTE, 0, 0, master_comm );
            MPI_Irecv(&nearby_node, 1, MPI_INT, 0, 0, master_comm, &mas_req);
        } else {
            report.reporting_node = -1;
            MPI_Send( &report, sizeof(NodeReport), MPI_BYTE, 0, 0, master_comm );
        }


        for (int i = 0; i < NUM_CHARGING_PORTS; i++) {
            pthread_cancel(ports[i].tid);
            pthread_join(ports[i].tid, NULL);
            pthread_mutex_destroy(&ports[i].mutex);
        }

        MPI_Test(&requests, &terminated, MPI_STATUS_IGNORE);
        sleep(3);
    }
    
    // for (int i = 0; i < size; i++) {
    //     printBuffer(&stationData[i], sFile);
    // }

    printf("Charging Node %d: Received termination signal. Shutting down...\n", my_rank);

    fclose(sFile);
    
    // Finalize MPI for this node
    MPI_Finalize();
    exit(0);
    MPI_Comm_free(&cart_comm);


    return 0;
}


int getTotalAvailability() {
    int totalAvailability = -1;

    for (int i = 0; i < NUM_CHARGING_PORTS; i++) {
        pthread_mutex_lock(&ports[i].mutex);
        totalAvailability += ports[i].availability;
        pthread_mutex_unlock(&ports[i].mutex);
    }

    return totalAvailability;
}

void *simulateChargingPort(void* arg) {
    ChargingPort* port = (ChargingPort*)arg;

    // Simulate periodic availability updates
    // Modify availability status (simplified for demonstration)
    pthread_mutex_lock(&port->mutex);
    port->availability = rand() % 2; // 0 or 1 (available or in use)
    pthread_mutex_unlock(&port->mutex);
    // printf("Port: %d Availability: %d\n", port->port_number, port->availability);

    return 0;
}

void initSharedArray(CircularArray* array) {
    array->front = 0;
    array->rear = -1;
    array->count = 0;
    pthread_mutex_init(&array->mutex, NULL);
}

void addToArray(CircularArray* array, ChargingNodeData item) {
    pthread_mutex_lock(&array->mutex);

    // Check if the array is full
    if (array->count == ARRAY_SIZE) {
        // Remove the oldest element (FIFO)
        array->front = (array->front + 1) % ARRAY_SIZE;
        array->count--;
    }

    // Add the new item to the rear of the array
    array->rear = (array->rear + 1) % ARRAY_SIZE;
    array->data[array->rear] = item;
    array->count++;

    pthread_mutex_unlock(&array->mutex);
}

void printBuffer(CircularArray* buffer, FILE *file) {
    
    pthread_mutex_lock(&buffer->mutex);

    for (int i = 0; i < buffer->count; i++) {
        int index = (buffer->front + i) % ARRAY_SIZE;
        fprintf(file, "Time: %d, Availability: %d\n", buffer->data[index].time, buffer->data[index].availability);
    }

    pthread_mutex_unlock(&buffer->mutex);
}

