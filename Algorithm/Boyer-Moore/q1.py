"""
FIT3155 - Assignment 1

Boyer Moore algorithm running the opposite direction
where the pattern is shifted leftwards under the text between iterations,
from the rightmost end of the text towards the left

@Author: Ong Jing Wei
@Student ID: 32909764

"""
import sys


def extended_bad_character_shift_rule(pattern):
    """
    This function is the extended bad character shift rule which used in Boyer-Moore
    string pattern searching algorithm. Since we are implementing Boyer-Moore in opposite
    direction, hence this extended bad character shift rule is scanning from left to right
    and maps each ASCII character in the pattern to its rightmost occurance in the pattern.

    Input:
        pattern: A string which we are going to search for

    Output:
        bad_arr: A 2D table of integer where each row corresponds to an ASCII character
                 and each column corresponds to a position in the pattern. This table helps
                 us to determine how far the pattern can be shifted when a mismatch occurs
    
    Time Complexity: O(m*n), where m is the size of the pattern and n is the size of ASCII table
    Space Complexity: O(m*n), where m is the size of the pattern and n is the size of ASCII table
    """
    size_of_table = 94 # Number of ASCII characters range from 33 to 126
    bad_arr = [[]] * size_of_table
    for i in range(size_of_table):
        bad_arr[i] = [None] * len(pattern)

    for i in range(1, len(pattern)):
        bad_arr[ord(pattern[i]) - 33][i] = i + 1
        for j in range(i-1, -1, -1):
            if bad_arr[ord(pattern[i]) - 33][j] is None:
                bad_arr[ord(pattern[i]) - 33][j] = i + 1
    return bad_arr


def z_algorithm(pattern):
    """
    This function is the z algorithm for pattern searching. Z algorithm finds all occurances
    of a pattern in a text.

    Input:
        pattern: A string which we are searching for

    Output:
        z_array: A list of integer where the i-th element is equal to the greatest number of
                 characters that match between the prefix 
    
    Time complexity: O(m), where m is the size of the pattern
    Space complexity: O(m), where m is the size of the pattern

    """

    if len(pattern) == 0:
        return []
    # Define an array to store the z values
    z_array = [0] * len(pattern)         # O(n) space   
    z_array[0] = len(pattern)            # O(1) time       

    # Define the left and right pointers
    left = 0
    right = 0   # right is exclusive
    
    i = 1 # Start from the second character since the first character is the length of pattern 
    
    while i < len(pattern):
        # Case 1: i is outside the z-box
        if i > right:
            left = i
            right = i
            while right < len(pattern) and pattern[right] == pattern[right - left]:
                right += 1
            z_array[i] = right - left # Calculate the size of the z-box
            right -= 1

        # Case 2: i is inside the z-box
        else:
            k = i - left
            remaining = right - i + 1

            # Case 2a
            if z_array[k] < remaining:
                z_array[i] = z_array[k]

            # Case 2b
            else:
                left = i
                while right < len(pattern) and pattern[right] == pattern[right - left]:
                    right += 1
                z_array[i] = right - left
                right -= 1
        i += 1

    return z_array



def reverse_z_algoritm(pattern):
    """
    This function is the reverse of Z algorithm which need to be used by match prefix function
    later. It computes z array for the reversed pattern and then reverses the z array again.

    Input:
        pattern: A string that we are searching for
    
    Output:
        z_suffix[::1]: An array of integers where the i-th element is equal to the greatest
        number of characters that match between the reversed pattern prefix and the 
        substring beginning at position i, but the array is reversed.

    Time complexity: O(m), where m is the size of the pattern
    Space complexity: O(m), where m is the size of the pattern
    
    """
    z_suffix = z_algorithm(pattern[::-1])
    return z_suffix[::-1]



def good_suffix_shift_rule(pattern):
    """
    This function is the good suffix shift rule which used in Boyer-Moore string searching
    algorithm. It returns an array that maps each suffix of the pattern to its rightmost 
    occurrence in the pattern. The array is used to determine how far the pattern can be 
    shifted when a mismatch occurs during the search.

    Input:
        pattern: A string that we are searching for
    
    Output:
        good_suffix: An array where each index i corresponds to a suffix of the pattern 
                     of length i.

    Time complexity: O(m), where m is the size of the pattern
    Space complexity: O(m), where m is the size of the pattern
    
    """
    z_suffix = z_algorithm(pattern)
    good_suffix = [0] * (len(pattern)+1)
    m = len(pattern)

    for p in range(m-1, -1, -1):
        j = z_suffix[p] 
        good_suffix[j] = p 
    return good_suffix


def match_prefix(pattern):
    """
    This function is the match prefix which used in Boyer-Moore string searching algorithm.
    It computes the length of the longest prefix of the pattern that matches the suffix.

    Input:
        pattern: A string that we are searching for

    Output:
        match_arr: An array where the value at each index is the length of the longest 
        prefix of the pattern that matches a suffix ending at position i

    Time complexity: O(m), where m is the size of the pattern
    Space complexity: O(m), where m is the size of the pattern
    
    """
    m = len(pattern)
    z = reverse_z_algoritm(pattern)
    match_arr = [0] * (m+1)
    match_arr[0] = m

    for i in range(m):
        if z[i] + (m-i) - 1 == m:
            match_arr[i] = z[i]
        else:
            match_arr[i] = match_arr[i-1]
    
    return match_arr




def boyer_moore(text, pattern):
    """
    This function is the Boyer-Moore string searching algorithm. It searches for the exact
    pattern in a given text. The Boyer-Moore algorithm uses two preprocessing functions which
    are the extended bad character shift rule and the good suffix shift rule to determine how
    many steps should we shift the pattern when a mismatch occurs. 

    Input:
        text: A string which we are going to search for the pattern
        pattern: A string which we are going to search for

    Output:
        res_match: A list of integers with all the starting indices of the match pattern in 1-based

    Time complexity: O(n + m), where n is the size of the text and m is the size of the pattern
    Space complexity: O(m), due to the creation of the bc_table, gs_table, and mp_table arrays

    """
    m = len(pattern)
    bc_table = extended_bad_character_shift_rule(pattern)
    gs_table = good_suffix_shift_rule(pattern)
    mp_table = match_prefix(pattern)

    start = len(text) - m
    startpoint = m
    endpoint = m

    res_match = []

    if m == 0:
        return res_match
    
    while start >= 0:
        k = 0

        while k < m:

            if k == startpoint: # Skip and resume from the startpoint
                k = endpoint
                continue

            if pattern[k] != text[start + k]:
                break
            k += 1

        # Case 1: All Matched     
        if k >= m:   
            res_match.append(start+1) # since the start index is 0 based, we add 1 to make it 1-based indexing
            start -= m - mp_table[m-2]
            
            # Reset the startpoint and resume point to default
            startpoint = m
            endpoint = m
        # Case 2: Mismatch
        else:
            # Apply extended bad character rule
            bc_table_val = bc_table[ord(text[start + k]) - 33][k] # Get the rightmost occurance of the bad character in the ebc_table table

            if bc_table_val == None : # If the bad character is not in the pattern
                bc_table_shift = m - k
                startpoint = m
                endpoint = m

            else:
                bc_table_shift = bc_table_val - k - 1 
                startpoint = bc_table_shift + k
                endpoint = startpoint + 1 


            bc_table_shift = max(1, bc_table_shift)
            bc_startpoint = startpoint
            bc_endpoint = endpoint

            # Apply good suffix rule
            gs_table_val = gs_table[k]
            if gs_table_val == 0: # It means there is no rightmost occurance of the suffix of the pattern
                gs_table_shift = len(pattern) - mp_table[k] 
                startpoint = mp_table[k] 
                endpoint = startpoint + 1
            else:
                gs_table_shift = gs_table_val 
                startpoint = gs_table_val 
                endpoint = k + gs_table_val

            gs_startpoint = startpoint
            gs_endpoint = endpoint

            if bc_table_shift >= gs_table_shift:
                shift = bc_table_shift
                startpoint = bc_startpoint
                endpoint = bc_endpoint

            else:
                shift = gs_table_shift
                startpoint = gs_startpoint
                endpoint = gs_endpoint

            start -= shift
        
    with open("output_q1.txt","w") as f:
        for i in res_match:
            f.write(i.__str__() + "\n")
        f.close()
        
    return res_match



#this function reads a file and return its content
def read_file(file_path: str) -> str:
   f = open(file_path, 'r')
   line = f.readlines()
   f.close()
   return line

if __name__ == '__main__':
    #retrieve the file paths from the commandline arguments
    _, filename1, filename2 = sys.argv
    print("Number of arguments passed : ", len(sys.argv))
    # since we know the program takes two arguments
    print("First argument : ", filename1)
    print("Second argument : ", filename2)
    file1content = read_file(filename1)
    print("\nContent of first file : ", file1content)
    file2content = read_file(filename2)
    print("\nContent of second file : ", file2content)

    txt=file1content[0]
    pat=file2content[0]
    boyer_moore(txt,pat)



