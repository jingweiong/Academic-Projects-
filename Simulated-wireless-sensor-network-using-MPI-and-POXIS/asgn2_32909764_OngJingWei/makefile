all: asgn2

asgn2: asgn2.c  
	mpicc asgn2.c -o asgn2.o -lpthread -fopenmp

run:
	mpirun -np 10 -oversubscribe asgn2.o 3 3   
