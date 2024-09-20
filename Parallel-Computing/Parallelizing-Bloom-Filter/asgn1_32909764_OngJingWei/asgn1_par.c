/*

FIT3143 Assignment 1

Name: Ong Jing Wei
Student ID: 32909764

*/

#include <stdio.h>
#include <math.h>
#include <stdlib.h>
#include <time.h>
#include <string.h>
#include <ctype.h>
#include <omp.h>
#include <pthread.h>

#define BITS_PER_CHAR (sizeof(char) * 8)
#define WORD_LIST_COUNT 966068
#define QUERY_LIST_COUNT 91640
#define FALSE_POSITIVE_RATE 0.01

// function prototype
int calculate_bitsize(int n, double fp);
char *create_bitarray(int bitsize);
int calculate_hash(int size, int num_element);
int hash1(char *word, int bitsize);
int hash2(char *word, int bitsize);
int hash3(char *word, int bitsize);
void add(char *word, char *bit_array, int bitsize, int (*hashFunctions[])(char *, int));
int check(char *word, char *bit_array, int bitsize, int(*hashFunctions[])(char *, int));
void setBit(char *bitarray, int index);
int checkBit(char *bitarray, int index);
void clear_bits(char *bitarray);
int isWordInList(char *word, char **wordList, int numWords);
int readFile(int filelength, char ***pppArray, char *filename);
void lookup(char *filename, char *bit_array, int bitsize, int (*hashFunctions[])(char *, int), char ***pppArray, int lengthArray);

int main()
{

    int bitsize, hash_num, wordListLengthArray;
    char *bit_array;
    char ** ppWordListArray;
    char * datasetFile = "Shakespeare_dataset.txt";
    // char * datasetFile = "mobydick_dataset.txt";
    char * queriesFile = "query_dataset.txt";
    // char * datasetFile = "clean_dataset-string-matching_train.txt";
    // char * queriesFile = "dataset-queries.txt";

    int (*hashFunctions[])(char *, int) = {hash1, hash2, hash3};

    struct timespec start, end, startAdd, endAdd, startLookup, endLookup;
    double timetaken;

    clock_gettime(CLOCK_MONOTONIC, &start); 

    // calculate the required bit size to create the bit array
    // It is divided by 10 to better illustrate the false positive
    bitsize = calculate_bitsize(WORD_LIST_COUNT, FALSE_POSITIVE_RATE)/10;

    // creating a bit array
    bit_array = create_bitarray(bitsize);

    // calling function to read word list file
    wordListLengthArray = readFile(WORD_LIST_COUNT, &ppWordListArray, datasetFile);
    printf("Total unique words: %d\n", wordListLengthArray);

    // adding the words into the bit array
    clock_gettime(CLOCK_MONOTONIC, &startAdd);
    #pragma omp parallel for schedule(static)
    for (int i = 0; i < wordListLengthArray; i++)
    {
        add(*(ppWordListArray+i), bit_array, bitsize, hashFunctions);
    } 
    clock_gettime(CLOCK_MONOTONIC, &endAdd);

    // lookup the element inside the 
    clock_gettime(CLOCK_MONOTONIC, &startLookup);
    lookup(queriesFile, bit_array, bitsize, hashFunctions, &ppWordListArray, wordListLengthArray);
    clock_gettime(CLOCK_MONOTONIC, &endLookup);

    // free bits for the bit array
    clear_bits(bit_array);

    // count for the computational time
    timetaken = (endAdd.tv_sec - startAdd.tv_sec) * 1e9;
    timetaken = (timetaken + (endAdd.tv_nsec - startAdd.tv_nsec)) * 1e-9;
    printf("Time taken for insert: %lf\n",timetaken);

    timetaken = (endLookup.tv_sec - startLookup.tv_sec) * 1e9;
    timetaken = (timetaken + (endLookup.tv_nsec - startLookup.tv_nsec)) * 1e-9;
    printf("Time taken for lookup: %lf\n", timetaken);

    clock_gettime(CLOCK_MONOTONIC, &end);
    timetaken = (end.tv_sec - start.tv_sec) * 1e9;
    timetaken = (timetaken + (end.tv_nsec - start.tv_nsec)) * 1e-9;
    printf("Overall time: %lf\n", timetaken);

    return 0;
}

/// @brief Read the word list file and count for unique words
/// @param filelength length of word list file
/// @param pppArray word list array
/// @param filename filename
/// @return unique word list length
int readFile(int filelength, char ***pppArray, char *filename)
{
    struct timespec start, end;
    double timetaken;

    FILE *file = fopen(filename, "r");
    if (file == NULL){
        perror("Error opening file");
        return -1;
    }

    char line[60];
    char **fileWordList = NULL;
    char **uniqueWordList = NULL;
    int uniqueWordListLength = 0;
    int wordcount = 0;

    fileWordList = (char **)malloc(filelength * sizeof(char *));
    for (int i = 0; i < filelength; i++)
    {
        fgets(line, sizeof(line), file);
        fileWordList[i] = strdup(line);
    }
    uniqueWordList = (char **)malloc(filelength * sizeof(char *));
    fclose(file);

    clock_gettime(CLOCK_MONOTONIC, &start); 
    #pragma omp parallel for schedule(dynamic)
    for (int i = 0; i < filelength; i++)
    {
        for (int j = 0; j < fileWordList[i][j]; j++)
        {
            fileWordList[i][j] = tolower(fileWordList[i][j]);
        }
        if (!isWordInList(fileWordList[i], uniqueWordList, uniqueWordListLength))
        
        {
            #pragma omp critical
            {
                uniqueWordList[uniqueWordListLength] = strdup(fileWordList[i]);
                uniqueWordListLength++;
            }
        }
    }
    clock_gettime(CLOCK_MONOTONIC, &end); 
    timetaken = (end.tv_sec - start.tv_sec) * 1e9;
    timetaken = (timetaken + (end.tv_nsec - start.tv_nsec)) * 1e-9;
    printf("Time taken for counting unique words: %lf\n", timetaken);

    uniqueWordList = realloc(uniqueWordList, uniqueWordListLength * sizeof(char *));
    *pppArray = uniqueWordList;

    // free the array
    for (int i = 0; i < filelength; i++)
    {
        free(fileWordList[i]);
    }
    free(fileWordList);
    return uniqueWordListLength;
    
}

/// @brief check if the word is in the input list
/// @param word 
/// @param wordList 
/// @param numWords number of words
/// @return 1 if word exist in the list else 0
int isWordInList(char *word, char **wordList, int numWords)
{
    for (int i = 0; i < numWords; i++)
    {
        if (strcmp(word, wordList[i]) == 0)
        {
            return 1;
        }
    }
    return 0;
}

/// @brief Read the query list file and check if the word is inside the bitarray
/// @param filename filename
/// @param bit_array bit array
/// @param bitsize bit size
/// @param hashFunctions array containing different hash functions
/// @param pppArray array
/// @param lengthArray length of array
void lookup(char *filename, char *bit_array, int bitsize, int (*hashFunctions[])(char *, int), char ***pppArray, int lengthArray)
{
    FILE *file = fopen(filename, "r");
    char line[60];
    char **FileWordList = NULL;
    int truepositive = 0;
    int truenegative = 0;
    int falsepositive = 0;
    int falsenegative = 0;

     if (file == NULL)
    {
        perror("Error opening file");
    }

    FileWordList = (char **)malloc(QUERY_LIST_COUNT * sizeof(char *));
    for (int i = 0; i < QUERY_LIST_COUNT; i++)
    {
        fgets(line, sizeof(line), file);
        FileWordList[i] = strdup(line);
    }


    #pragma omp parallel for schedule(dynamic) 
    for (int i = 0; i < QUERY_LIST_COUNT; i++)
    {
        for (int j = 0; j < FileWordList[i][j]; j++)
        {
            FileWordList[i][j] = tolower(FileWordList[i][j]);
        }
        int res = check(FileWordList[i], bit_array, bitsize, hashFunctions);
        int isInList = isWordInList(FileWordList[i], *pppArray, lengthArray);
        if (res == 1)   
        {
            if (isInList == 0)
            {
                falsepositive++;
            } 
            else
            {
                truepositive++;
            }
        } 
        else
        {
            if (isInList == 0)
            {
                truenegative++;
            } 
            else
            {
                falsenegative++;
            }
        }
        
    }

    for (int i = 0; i < QUERY_LIST_COUNT; i++)
    {
        free(FileWordList[i]);
    }
    free(FileWordList);

    printf("Total number of queries: %d\n", QUERY_LIST_COUNT);
    printf("Number of true positive: %d\n", truepositive);
    printf("Number of true negative: %d\n", truenegative);
    printf("Number of false positive: %d\n", falsepositive);
    printf("Number of false negative: %d\n", falsenegative);
}

/// @brief Add the word into the bit array by hashing it and set the bit array
/// @param word word
/// @param bit_array bit array 
/// @param bitsize  bit size
/// @param hashFunctions array containing different hash functions
void add(char *word, char *bit_array, int bitsize, int (*hashFunctions[])(char *, int))
{
    for (int i = 0; i<3; i++)
    {
        int index = hashFunctions[i](word, bitsize);
        setBit(bit_array, index);
    }
}

/// @brief Check if the word is exist in bit array
/// @param word word
/// @param bit_array bit array 
/// @param bitsize bit size
/// @param hashFunctions array containing different hash functions
/// @return 
int check(char *word, char *bit_array, int bitsize, int(*hashFunctions[])(char *, int))
{
    int present = 1;
    for (int i = 0; i < 3; i++)
    {
        int index = hashFunctions[i](word, bitsize);
        if (checkBit(bit_array, index) == 0) present = 0;
    }                                                                                                     
    return present;
    
}

/// @brief calculate bit size required using formula
/// @param n number of words to be inserted
/// @param fp false positive rate
/// @return bit size
int calculate_bitsize(int n, double fp)
{
    return ((-n * log(fp)) / (pow(log(2), 2)));
}

/// @brief create the bit array
/// @param bitsize bitsize
/// @return bit array
char *create_bitarray(int bitsize)
{
    char *bitarray = (char*)malloc((bitsize + 32) / (BITS_PER_CHAR));

    return bitarray;
}

/// @brief Take in the hash index and flip the bit inside bit array
/// @param bitarray bit array
/// @param index hash index
void setBit(char *bitarray, int index)
{
    bitarray[index / BITS_PER_CHAR] |= (1 << (index % BITS_PER_CHAR));
}

/// @brief check the bit inside the bit array with given index
/// @param bitarray bit array
/// @param index index
/// @return 
int checkBit(char *bitarray, int index)
{
    return (bitarray[index / BITS_PER_CHAR] & (1 << (index % BITS_PER_CHAR))) != 0;
}

/// @brief free the bits
/// @param bitarray bit array
void clear_bits(char *bitarray)
{
    free(bitarray);
}

// Referred to djb2 hash function
int hash1(char *word, int bitsize)
{
    unsigned long hash = 5381;
    while(*word)
    {
        hash = (hash << 5) + hash + *word;
        word++;
    }
    return (hash % bitsize);
}

// Referred to djb2 hash function
int hash2(char *word, int bitsize)
{
    unsigned long hash = 5381;
    while (*word)
    {
        hash = (hash << 7) - hash + *word;
        word++;
    }
    return (hash % bitsize);
}

// Referred to sdbm hash function
int hash3(char *word, int bitsize)
{
    unsigned long hash = 0;
    while (*word)
    {
        hash = *word + (hash << 6) + (hash << 16) - hash;
        word++;
    }
    return (hash % bitsize);
}

/// @brief calculate number of required hash functions
/// @param size 
/// @param num_element 
/// @return number of required hash functions
int calculate_hash(int size, int num_element)
{
    return ((size/num_element) * log(2));
}





