/**
 * @file parallel_pipeline.c
 * @brief This file contains the parallel implementation of the DES algorithm using pipeline parallelism.
 * @details The second approach for parallelizing our system is based
            on performing a fraction of totally 16 rounds of DES at each
            processor. We call this method as pipeline scheme. This
            scheme incorporates a round based parallelization to
            complete the 16 rounds of iteration. With pipeline scheme,
            we distribute the internal rounds of each block encryption to
            each processor. Regarding the 3 load distribution layouts,
            with 4 node parallelization each node will perform four of
            these rounds, with 8 node parallelization each node will
            perform two of these rounds and with 16 node (maximum)
            parallelization, each node will perform only one of these
            rounds. Depending on how many processors (4, 8 or 16) are
            used in the current group, the scheme passes the encryption
            to the next processor.
 * @author Ong Jing Wei
 * @date 2023-10-28
*/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include <math.h>
#include "numberConversions.h"
#include "matrices.h"
#include <omp.h>

#define NUMPROCESSORS 4

char* encryption(const char* pt, char** roundKeys, int round);
char* permutation(const char* str, int* arr, int n);
char* shift_left(char* k, int shifts);
char* xorOperation(const char* str1, const char* str2);


int main() {

    struct timespec start_enc, end_enc, start_dec, end_dec;
    double timetaken;
    char* pt[100];
    int pt_count = 0;
    char c, temp[9];

    clock_gettime(CLOCK_MONOTONIC, &start_enc);

    // Reading from file and ensure the length of plaintext is a multiple of 8 by adding spaces if necessary
    FILE* file = fopen("plaintext.txt", "r");
    if (file == NULL) {
        perror("Error opening file");
        return 1;
    }

    while ((c = fgetc(file)) != EOF) {
        temp[pt_count % 8] = c;
        pt_count++;
        if (pt_count % 8 == 0) {
            temp[8] = '\0';
            pt[pt_count / 8 - 1] = strdup(temp);
        }
    }

    if (pt_count % 8 != 0) {
        for (int i = pt_count % 8; i < 8; i++) {
            temp[i] = ' ';
        }
        temp[8] = '\0';
        pt[pt_count / 8] = strdup(temp);
    }

    fclose(file);

    char* key = "ABC12532110EDA56";
    key = convertToBinary(key);
    key = permutation(key, keyTransformation, 56);

    char* left = (char*)malloc(29);
    char* right = (char*)malloc(29);
    left[28] = '\0';
    right[28] = '\0';

    strncpy(left, key, 28);
    strncpy(right, key + 28, 28);

    char* roundKeys[16];

    for (int i = 0; i < 16; i++) {
        left = shift_left(left, shiftsMatrix[i]);
        right = shift_left(right, shiftsMatrix[i]);

        char* combinedKey = (char*)malloc(49);
        combinedKey[48] = '\0';
        strcat(combinedKey, left);
        strcat(combinedKey, right);

        char* roundKey = permutation(combinedKey, keyCompresssion, 48);
        roundKeys[i] = roundKey;
    }

    int numBlocks = pt_count / 8;
    // Determine the number of rounds each processor will handle
    int roundsPerProcessor = 16 / NUMPROCESSORS;

    char* ciphertext[100];
    printf("Encrypting...\n");

    // Create shared data arrays for passing data between processors
    char* left_data[NUMPROCESSORS];
    char* right_data[NUMPROCESSORS];
    for (int i = 0; i < NUMPROCESSORS; i++) {
        left_data[i] = (char*)malloc(33);
        right_data[i] = (char*)malloc(33);
    }

    for (int round = 0; round < roundsPerProcessor; round++) {
        #pragma omp parallel for
        for (int i = 0; i < numBlocks; i++) {
            char* cipher = encryption(pt[i], roundKeys, round);
            ciphertext[i] = cipher;
        }

        // If not the last round, pass data between processors
        if (round < roundsPerProcessor - 1) {
            int myThread = omp_get_thread_num();
            int nextThread = (myThread + 1) % NUMPROCESSORS;

            // Share left_data and right_data between adjacent threads
            #pragma omp barrier
            if (myThread == 0) {
                // Pass data from the last processor to the first processor
                strcpy(left_data[0], left_data[NUMPROCESSORS - 1]);
                strcpy(right_data[0], right_data[NUMPROCESSORS - 1]);
            } else {
                // Pass data from one processor to the next
                strcpy(left_data[nextThread], left_data[myThread]);
                strcpy(right_data[nextThread], right_data[myThread]);
            }
            #pragma omp barrier
        }
    }

    FILE* writeObj = fopen("encrypted.txt", "w");
    if (writeObj == NULL) {
        perror("Error opening file");
        return 1;
    }

    for (int i = 0; i < pt_count / 8; i++) {
        fprintf(writeObj, "%s", ciphertext[i]);
        free(ciphertext[i]);
    }

    fclose(writeObj);

    printf("Encryption Process Completed\n");

    for (int i = 0; i < 16; i++) {
        free(roundKeys[i]);
    }

    for (int i = 0; i < pt_count / 8; i++) {
        free(pt[i]);
    }

    free(left);
    free(right);

    clock_gettime(CLOCK_MONOTONIC, &end_enc);
    timetaken = (end_enc.tv_sec - start_enc.tv_sec) * 1e9;
    timetaken = (timetaken + (end_enc.tv_nsec - start_enc.tv_nsec)) * 1e-9;
    printf("Time taken for encryption: %lf\n", timetaken);

    return 0;
}

char* permutation(const char* str, int* arr, int n) {
    char* res = (char*)malloc(n + 1);
    res[n] = '\0';

    for (int i = 0; i < n; i++) {
        res[i] = str[arr[i] - 1];
    }

    return res;
}

char* shift_left(char* k, int shifts) {
    int n = strlen(k);
    char* s = (char*)malloc(n + 1);
    s[n] = '\0';

    for (int i = 0; i < shifts; i++) {
        for (int j = 1; j < n; j++) {
            s[j - 1] = k[j];
        }
        s[n - 1] = k[0];
        strcpy(k, s);
    }

    free(s);
    return k;
}

char* xorOperation(const char* str1, const char* str2) {
    int n = strlen(str1);
    char* xored = (char*)malloc(n + 1);
    xored[n] = '\0';

    for (int i = 0; i < n; i++) {
        if (str1[i] == str2[i])
            xored[i] = '0';
        else
            xored[i] = '1';
    }

    return xored;
}

char* encryption(const char* pt, char** roundKeys, int round) {
    char* pt_bin = str2bin(pt);
    char* pt_permuted = permutation(pt_bin, initialPermutation, 64);

    char* left = (char*)malloc(33);
    char* right = (char*)malloc(33);
    left[32] = '\0';
    right[32] = '\0';

    strncpy(left, pt_permuted, 32);
    strncpy(right, pt_permuted + 32, 32);

    for (int i = 0; i < 16; i++) {
        char* expandedRPT = permutation(right, dBox, 48);
        char* x = xorOperation(roundKeys[(round + i) % 16], expandedRPT); // Adjust the round key using (round + i) % 16

        char* result[8];
        char res[33];
        res[32] = '\0';

        for (int j = 0; j < 8; j++) {
            int row = 2 * (x[j * 6] - '0') + (x[j * 6 + 5] - '0');
            int col = 8 * (x[j * 6 + 1] - '0') + 4 * (x[j * 6 + 2] - '0') + 2 * (x[j * 6 + 3] - '0') + (x[j * 6 + 4] - '0');
            int val = sbox[j][row][col];
            result[j] = decimalToBinary(val);
        }

        res[0] = '\0';
        for (int j = 0; j < 8; j++) {
            strcat(res, result[j]);
        }

        char* p = permutation(res, pbox, 32);
        x = xorOperation(p, left);

        strcpy(left, x);

        if (i != 15) {
            char temp[33];
            strcpy(temp, left);
            strcpy(left, right);
            strcpy(right, temp);
        }
    }

    char* combined = (char*)malloc(65);
    combined[64] = '\0';
    strcat(combined, left);
    strcat(combined, right);

    char* cipher_bin = permutation(combined, finalPermutation, 64);
    char* cipher = bin2str(cipher_bin);

    free(pt_bin);
    free(pt_permuted);
    free(left);
    free(right);
    free(cipher_bin);

    return cipher;
}
