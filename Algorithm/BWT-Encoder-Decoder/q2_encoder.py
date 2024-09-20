"""
FIT3155 - Assignment 2

Question 2:
Burrows-Wheeler Transform based compression (encoder)

@Author: Ong Jing Wei
@Student ID: 32909764
"""
import sys
from q1 import SuffixTree
import heapq
from bitarray import bitarray

def get_suffix_array(text):
    """
    Returns the suffix array of the given text using Ukkonen's algorithm from q1

    Time complexity: O(n) (best case), where n is the length of the text
                   : O(n^2) (worst case), where n is the length of the text
    Space complexity: O(n), where n is the length of the text
    """
    suffix_tree = SuffixTree(text)
    suffix_tree.ukkonen()
    return suffix_tree.inorder_traversal(suffix_tree.root)

def bwt(text):
    """
    Burrows-Wheeler Transform of the given text

    Time complexity: O(n), where n is the length of the text
    Space complexity: O(n), where n is the length of the text
    """

    suffix_array = get_suffix_array(text)
    bwt_text = ""
    for i in suffix_array:
        if i == 0:
            bwt_text += text[-1]
        else:
            bwt_text += text[i-1]
    return bwt_text


def huffman_encoding(text):
    """
    Huffman encoding of the given text

    Time complexity: O(nlogn), where n is the length of the text
    Space complexity: O(n), where n total ascii characters required
    """
    freq = [None] * (126-36 + 1)
    for char in text:
        if freq[ord(char)-36] is None:
            freq[ord(char)-36] = 1
        else:
            freq[ord(char)-36] += 1

    heap = []
    for i in range(len(freq)):
        if freq[i] is not None:
            heapq.heappush(heap, (freq[i], chr(i+36)))
            freq[i] = None

    while len(heap) > 1:
        # serve twice at a time where first serve is 0 and second serve is 1
        first_serve = heapq.heappop(heap)
        second_serve = heapq.heappop(heap)

        combined_char = first_serve[1] + second_serve[1]
        combined_freq = first_serve[0] + second_serve[0] + ((len(first_serve[1]) + len(second_serve[1]))/10) # so that when equal freq, the order is preserved

        for char in first_serve[1]:
            if freq[ord(char)-36] is None:
                freq[ord(char)-36] = '0'
            else:
                freq[ord(char)-36] = '0' + freq[ord(char)-36]
        for char in second_serve[1]:
            if freq[ord(char)-36] is None:
                freq[ord(char)-36] = '1'
            else:
                freq[ord(char)-36] = '1' + freq[ord(char)-36]
        
        heapq.heappush(heap, (combined_freq, combined_char))

    return freq, heap

def convert_to_binary(num):
    """
    Function which convert the decimal number to binary

    Time complexity: O(n), where n is the decimal number
    Space complexity: O(n), where n is the decimal number
    """
    binary = ""
    while num > 0:
        binary = str(num % 2) + binary
        num = num // 2
    return binary

def elias_encoding(num):
    """
    Elias encoding of the given number
    
    Time complexity: O(n), where n is the number of bits required to represent the number
    Space complexity: O(n), where n is the number of bits required to represent the number
    """
    
    bit_array = convert_to_binary(num)
    length = len(bit_array)

    length = length - 1
    while length > 0:
        next_bit_array = convert_to_binary(length)
        length = len(next_bit_array)-1 
        next_bit_array = next_bit_array[1:]
        bit_array = '0' + next_bit_array + bit_array

    return bit_array    

def runlength_encoding(text):
    """
    Runlength encoding of the given text
    
    Time complexity: O(n), where n is the length of the text
    Space complexity: O(n), where n is the length of the text
    """
    
    runlength = 1
    list_of_tuples = []

    for i in range(1, len(text)):
        if text[i] == text[i-1]:
            runlength += 1
        else:
            list_of_tuples.append((text[i-1], runlength))
            runlength = 1

        if i == len(text)-1:
            list_of_tuples.append((text[i], runlength))
    
    return list_of_tuples

def get_7bit_ascii(char):
    """
    Function which converts the given character to 7-bit ascii
    
    Time complexity: O(1)
    Space complexity: O(1)
    """
    binary = convert_to_binary(ord(char))
    if len(binary) < 7:
        binary = '0'*(7-len(binary)) + binary       # make it to 7 bits
    return binary

def encoding(text):
    """
    Function which encodes the given text using BWT, Huffman, Elias and Runlength encoding
    to format it into a binary string

    Time complexity: O(nlogn), where n is the length of the text
    Space complexity: O(n), where n is the length of the text
    """

    encoded_text = ""
    bwt_text = bwt(text)
    freq, hm = huffman_encoding(bwt_text)
    num_unique_char = len(hm[0][1])

    length_bwt_encoded = elias_encoding(len(bwt_text))
    encoded_text += length_bwt_encoded
    num_unique_char_encoded = elias_encoding(num_unique_char)
    encoded_text += num_unique_char_encoded

    for char in hm[0][1]:
        ascii_encoded = get_7bit_ascii(char)
        encoded_text += ascii_encoded

        encoded_code_length = elias_encoding(len(freq[ord(char)-36]))
        encoded_text += encoded_code_length
        
        encoded_code = freq[ord(char)-36]
        encoded_text += encoded_code
        
    runlength = runlength_encoding(bwt_text)
    for char, runlength in runlength:
        encoded_code = freq[ord(char)-36]
        encoded_text += encoded_code

        encoded_runlength = elias_encoding(runlength)
        encoded_text += encoded_runlength

    return encoded_text


class WriteFile:
    """
    Class which writes the encoded text to a binary file
    """
    def __init__(self, encoded_text):
        self.encoded_text = encoded_text
        self.file = open("q2_encoder_output.bin", 'wb')
        self.write_to_file()
        self.close()
    
    def write_to_file(self):
        """
        Function which writes the encoded text to a binary file by eight bits at a time
        """
        while len(self.encoded_text) >= 8:
            byte = self.encoded_text[:8]
            self.encoded_text = self.encoded_text[8:]
            num = int(byte, 2)
            mybyte = num.to_bytes(1, byteorder='big')
            self.file.write(mybyte)

        if len(self.encoded_text) > 0:
            byte = self.encoded_text + '0'*(8-len(self.encoded_text))
            num = int(byte, 2)
            mybyte = num.to_bytes(1, byteorder='big')
            self.file.write(mybyte)
    
    def close(self):
        """
        Function which closes the file
        """
        self.file.close()
    





def read_file(file_path: str) -> str:
    f = open(file_path, 'r')
    line = f.readlines()
    f.close()
    return line

if __name__ == "_main_":
    # #retrieve the file paths from the commandline arguments
    _, filename1= sys.argv
    print("Number of arguments passed : ", len(sys.argv))
    # since we know the program takes two arguments
    print("Argument : ", filename1)
    file1content = read_file(filename1)
    print("\nContent of the file : ", file1content)

    encoded_text = encoding(file1content)
    write = WriteFile(encoded_text)

    
    
    

    



