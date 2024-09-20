all: asgn1_serial asgn1_par; 

asgn1_serial: asgn1_serial.c ; gcc asgn1_serial.c -o asgn1_serial.o -lm

asgn1_par: asgn1_par.c ; clang -fopenmp asgn1_par.c -o asgn1_par.o -lomp


