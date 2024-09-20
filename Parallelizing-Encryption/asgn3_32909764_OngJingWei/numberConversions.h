#include <stdio.h>
#include <string.h>

#define numberConversions

char* str2bin(const char* s) 
{
    char* binary = malloc(strlen(s) * 8 + 1);
    binary[0] = '\0';

    for (int i = 0; s[i] != '\0'; i++) {
        for (int j = 7; j >= 0; j--) {
            int bit = (s[i] >> j) & 1;
            char bit_char = bit + '0';
            strncat(binary, &bit_char, 1);
        }
    }

    return binary;
}

char* bin2str(const char* str) 
{
    char* s = malloc(strlen(str) / 8 + 1);
    s[0] = '\0';

    for (int i = 0; i < strlen(str); i += 8) {
        char bin[9] = {'\0'};
        strncpy(bin, str + i, 8);
        char c = 0;
        for (int j = 0; j < 8; j++) {
            c = (c << 1) | (bin[j] - '0');
        }
        strncat(s, &c, 1);
    }

    return s;
}

char* decimalToBinary(int val) 
{
    char res[5] = {'\0'};
    res[0] = (val / 8) + '0';
    val = val % 8;
    res[1] = (val / 4) + '0';
    val = val % 4;
    res[2] = (val / 2) + '0';
    val = val % 2;
    res[3] = val + '0';
    return strdup(res);
}

char* convertToBinary(const char* s) 
{
    char* res = malloc(strlen(s) * 4 + 1);
    res[0] = '\0';

    for (int i = 0; s[i] != '\0'; i++) {
        switch (s[i]) {
        case '0': 
            strcat(res, "0000");
            break;
        case '1': 
            strcat(res, "0001");
            break;
        case '2': 
            strcat(res, "0010");
            break;
        case '3': 
            strcat(res, "0011");
            break;
        case '4': 
            strcat(res, "0100");
            break;
        case '5': 
            strcat(res, "0101");
            break;
        case '6': 
            strcat(res, "0110");
            break;
        case '7': 
            strcat(res, "0111");
            break;
        case '8': 
            strcat(res, "1000");
            break;
        case '9': 
            strcat(res, "1001");
            break;
        case 'A': 
        case 'a': 
            strcat(res, "1010");
            break;
        case 'B': 
        case 'b': 
            strcat(res, "1011");
            break;
        case 'C': 
        case 'c': 
            strcat(res, "1100");
            break;
        case 'D': 
        case 'd': 
            strcat(res, "1101");
            break;
        case 'E': 
        case 'e': 
            strcat(res, "1110");
            break;
        case 'F': 
        case 'f': 
            strcat(res, "1111");
            break;
        }
    }

    return res;
}
