"""
FIT3155 - Assignment 2

Question 2:
Burrows-Wheeler Transform based compression (decoder)

@Author: Ong Jing Wei
@Student ID: 32909764
"""
import sys
from bitarray import bitarray

class Decoder:
    """
    Decoder class for decoding the compressed file
    """
    def __init__(self, filename):
        self.file = open(filename, "rb")

        byte_from_file = self.file.read(1)
        byte_to_integer = int.from_bytes(byte_from_file, 'big') # convert byte to integer
        self.curr_bytes = (1 << 8) | byte_to_integer

        self.file.seek(1)  # skip the first byte after reading it
        self.pointer = 1

    def read_next_byte(self):
        """
        Read the next byte from the file
        
        Time complexity: O(1)
        Space complexity: O(1)
        """
        byte_from_file = self.file.read(1)
        if byte_from_file:
            byte_to_integer = int.from_bytes(byte_from_file, 'big')
            self.curr_bytes = (self.curr_bytes << 8) | byte_to_integer
            self.pointer += 1
            self.file.seek(self.pointer)

    def bit_length(self, bit):
        """
        Calculate the length of the binary representation of the number
        
        Time complexity: O(logn), where n is the number of bits
        Space complexity: O(1)
        """
        length = 0
        while bit:
            bit >>= 1
            length += 1
        return length

    def get_bit(self, num, position):
        """
        Get the bit at the specified position
        
        Time complexity: O(1)
        Space complexity: O(1)
        """
        return (num & 1 << (self.bit_length(num) - position - 1)) >> (self.bit_length(num) - position - 1)
    
    def get_bit_range(self, number, start, end):
        """
        Get the bits in the specified range
        
        Time complexity: O(1)
        Space complexity: O(1)
        """
        num_length = self.bit_length(number)
        
        # Calculate the distance to shift to retain the desired bits
        shift_distance = num_length - end - 1
        
        # Shift the number to align the desired bits at the end
        shifted_number = number >> shift_distance
        
        # Generate a mask to isolate the desired bit segment
        mask = (1 << (end - start + 1)) - 1
        
        # Apply the mask to extract the desired segment and append a leading 1 bit
        result = (shifted_number & mask) | (1 << (end - start + 1))
    
        return result
    
    def convert_to_integer(self, bitarray):
        """
        Convert the bitarray to an integer
        
        Time complexity: O(n), where n is the number of bits in the bitarray
        Space complexity: O(1)
        """
        integer = 0
        for bit in bitarray:
            integer = (integer << 1) | bit
        return integer


    def elias_decoding(self):
        """
        Elias decoding of the given number
        
        Time complexity: O(n), where n is the number of bits in the number
        Space complexity: O(1)
        """

        bits = bitarray()
        length = 1

        # check if enough bits to decode
        if length+2 >= self.bit_length(self.curr_bytes) - 1:
            self.read_next_byte()

        if self.get_bit(self.curr_bytes, 1) == 1:
            self.curr_bytes = self.get_bit_range(self.curr_bytes, 2, self.bit_length(self.curr_bytes) - 1)
            return 1

        length += 1
        self.curr_bytes = self.get_bit_range(self.curr_bytes, length, self.bit_length(self.curr_bytes) - 1)
        msb = self.get_bit(self.curr_bytes, 1)  # get the most significant bit

        # check if most significant bit is not 1
        while msb != 1:
            if length >= self.bit_length(self.curr_bytes) - 1:
                self.read_next_byte()

            for i in range(length):
                bits.append(self.get_bit(self.curr_bytes, i+1))
            
            bits[0] = 1
            length_of_bits_to_remove = self.bit_length(self.convert_to_integer(bits)) + 1
            length = self.convert_to_integer(bits) + 1
            # remove the bits that have been decoded
            self.curr_bytes = self.get_bit_range(self.curr_bytes, length_of_bits_to_remove, self.bit_length(self.curr_bytes) - 1)
            msb = self.get_bit(self.curr_bytes, 1)
            bits = bitarray()   # reset bits

        if length >= self.bit_length(self.curr_bytes) - 1:
            self.read_next_byte()

        for i in range(length):
            bits.append(self.get_bit(self.curr_bytes, i+1))

        res = self.convert_to_integer(bits)
        self.curr_bytes = self.get_bit_range(self.curr_bytes, length+1, self.bit_length(self.curr_bytes) - 1)
        
        return res
    
    def decode_7bit_ascii(self):
        """
        Decode the 7-bit ASCII character

        Time complexity: O(1)
        Space complexity: O(1)
        """
        bits = bitarray()
        if self.bit_length(self.curr_bytes) - 1 < 7:
            self.read_next_byte()
        for i in range(7):
            bits.append(self.get_bit(self.curr_bytes, i+1))
        char = chr(self.convert_to_integer(bits))
        # remove the bits that have been decoded
        self.curr_bytes = self.get_bit_range(self.curr_bytes, 8, self.bit_length(self.curr_bytes) - 1)
        
        return char
    
    def huffman_decoding(self, char, code_length):
        """
        Huffman decoding of the character
        
        Time complexity: O(n), where n is the code length
        Space complexity: O(1)
        """
        bits = ""
        if self.bit_length(self.curr_bytes) - 1 < code_length:
            self.read_next_byte()

        # get the required bits to decode
        for i in range(code_length):
            bits += str(self.get_bit(self.curr_bytes, i+1))

        self.curr_bytes = self.get_bit_range(self.curr_bytes, code_length+1, self.bit_length(self.curr_bytes) - 1)
        return bits
    
    def runlength_decoding(self, bst):
        """
        Runlength decoding of the character
        
        Time complexity: O(n), where n is the number of bits in the huffman code
        Space complexity: O(1)
        """
        decoded_bwt_text = None
        bits = ""
        if self.bit_length(self.curr_bytes) - 1 < 8:
                self.read_next_byte()
        curr_bit = self.get_bit(self.curr_bytes, 1)
        bits += str(curr_bit)

        # decode the huffman code to get the character
        while decoded_bwt_text is None:

            huffman_code = bits
            char = bst[huffman_code]
            decoded_bwt_text = char
            self.curr_bytes = self.get_bit_range(self.curr_bytes, 2, self.bit_length(self.curr_bytes) - 1)
            curr_bit = self.get_bit(self.curr_bytes, 1)
            bits += str(curr_bit)

        # decode the runlength using elias decoding
        runlength = self.elias_decoding()

        return decoded_bwt_text, runlength
    
    def bwt_decoding(self, bwt_text):
        """
        Burrows-Wheeler Transform decoding

        Time complexity: O(n), where n is the length of the BWT text
        Space complexity: O(n), where n is the length of the BWT text
        """
        rank = [None] * (126 - 36 + 1)
        sorted_text = sorted(bwt_text)
        original_text = ""
        pos = 0

        # create rank table
        for i in range(len(bwt_text)):
            if rank[ord(sorted_text[i]) - 36] is None:
                rank[ord(sorted_text[i]) - 36] = i

        for i in range(len(bwt_text)):
            original_text += sorted_text[pos]
            n_occurrences = 0

            # check the number of occurrences of the character before the current position
            for k in range(pos):
                if bwt_text[k] == bwt_text[pos]:
                    n_occurrences += 1
            
            # new position is calculated based on the rank of the character in the rank table and the number of occurrences
            pos = rank[ord(bwt_text[pos]) - 36] + n_occurrences

        return original_text[::-1]
            
                

            
    def decode(self):
        """
        Decode the compressed file

        Time complexity: O(n), where n is the length of the compressed file
        Space complexity: O(n), where n is the length of the compressed file
        """
        bst = HuffmanBinarySearchTree()
        bwt_length = self.elias_decoding()
        num_unique_char = self.elias_decoding()

        for i in range(num_unique_char):
            char = self.decode_7bit_ascii()
            code_length = self.elias_decoding()
            huffman_code = self.huffman_decoding(char, code_length)
            bst[huffman_code] = char

        total_runlength = 0
        bwt_text = ""

        while total_runlength < bwt_length:
            decoded_bwt_text, runlength = self.runlength_decoding(bst)
            total_runlength += runlength
            for _ in range(runlength):
                bwt_text += decoded_bwt_text


        bwt_decoded = self.bwt_decoding(bwt_text)
        self.file.close()

        with open("q2_decoder_output.txt", "w") as f:
            f.write(bwt_decoded)
            f.close()

        return bwt_decoded
        


class BinaryStringKey:

    """ Custom class for binary string keys. """

    def __init__(self, string):
        self.string = string

    def __lt__(self, other):
        """ Implement less than comparison. """
        return self.string < other.string

    def __eq__(self, other):
        """ Implement equality comparison. """
        if isinstance(other, BinaryStringKey):
            return self.string == other.string
        return False

    def __str__(self):
        """ Return the string representation of the key. """
        return self.string


class TreeNode:
    """ Node class represent BST nodes. From FIT1008 """

    def __init__(self, key, item):
        self.key = key
        self.item = item
        self.left = None
        self.right = None

    def __str__(self):
        key = str(self.key) if type(self.key) != str else "'{0}'".format(self.key)
        item = str(self.item) if type(self.item) != str else "'{0}'".format(self.item)
        return '({0}, {1})'.format(key, item)


class HuffmanBinarySearchTree:
    """ Basic binary search tree for Huffman encoding. From FIT1008"""

    def __init__(self):
        self.root = None
        self.length = 0

    def is_empty(self):
        """ Check if the bst is empty. """
        return self.root is None

    def __len__(self):
        """ Return the number of nodes in the tree. """
        return self.length

    def __contains__(self, key):
        """ Check if the key is in the BST. """
        try:
            _ = self[key]
        except KeyError:
            return False
        else:
            return True

    def __getitem__(self, key):
        """ Get an item in the tree using the key. """
        return self.getitem_aux(self.root, BinaryStringKey(key))
    
    def getitem_aux(self, current, key):
        if current is None:
            return None
        elif key == current.key:
            return current.item
        elif key < current.key:
            return self.getitem_aux(current.left, key)
        else:
            return self.getitem_aux(current.right, key)

    def __setitem__(self, key, item):
        """ Insert an item into the tree using the key. """
        self.root = self.insert_aux(self.root, BinaryStringKey(key), item)

    def insert_aux(self, current, key, item):
        if current is None:
            current = TreeNode(key, item)
            self.length += 1
        elif key < current.key:
            current.left = self.insert_aux(current.left, key, item)
        elif key > current.key:
            current.right = self.insert_aux(current.right, key, item)
        else:
            raise ValueError('Inserting duplicate item')
        return current

    def is_leaf(self, current):
        """ Check whether or not the node is a leaf. """
        return current.left is None and current.right is None


if __name__ == "__main__":
    _, filename1= sys.argv
    print("Number of arguments passed : ", len(sys.argv))
    print("Argument : ", filename1)
    decoder = Decoder(filename1)
    decoded_text = decoder.decode()

    
    
    
    
    
