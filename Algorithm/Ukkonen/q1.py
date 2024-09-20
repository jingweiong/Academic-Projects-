"""
FIT3155 - Assignment 2

Question 1: 
Computes ranks of a list of suffixes

@Author: Ong Jing Wei
@Student ID: 32909764
"""
import sys

class End:
    """
    A class representing the end value.
    """
    def __init__(self, end):
        self.end = end
    
    def increment(self):
        self.end += 1
    
    def __str__(self):
        return str(self.end)
    
class Node:
    """
    Represents a node in a suffix tree.
    """

    def __init__(self, start=None, end=None, suffix_link=None, is_leaf=False):
        """
        Constructor
        start: The starting index of this node
        end: The ending index of this node
        suffix_link: The suffix link of this node
        children: List of child nodes
        is_leaf: Indicates whether this node is a leaf node
        suffix_id: The ID of the suffix represented by this node
        """
        self.start = start
        self.end = end
        self.suffix_link = suffix_link
        self.children = [None] * (126 - 36 + 1)
        self.is_leaf = is_leaf
        self.suffix_id = None

    def add_edge(self, char, child):
        """
        Adds an edge to the child node

        Time complexity: O(1)
        Space complexity: O(1)
        """
        self.children[ord(char) - 36] = child

    def get_edge(self, char):
        """
        Retrieves the child node corresponding to the given character

        Time complexity: O(1)
        Space complexity: O(1)
        """
        return self.children[ord(char) - 36]

    def get_end(self):
        """
        Retrieves the ending index of the substring represented by this node

        Time complexity: O(1)
        Space complexity: O(1)
        """
        if self.is_leaf:
            return self.end.end
        else:
            return self.end

    def __str__(self):
        """
        Returns a string representation of the node
        """
        return "Start: " + str(self.start) + " End: " + str(self.end)
    
class SuffixTree:
    """
    Suffix Tree class for ukkonen's algorithm
    """
    def __init__(self, text):
        """
        text: The input text
        root: The root node of the suffix tree
        active_node: The currently active node during construction of the suffix tree
        active_edge: The currently active edge during construction of the suffix tree
        active_length: The length of the active substring during construction of the suffix tree
        """
        self.text = text
        self.root = Node()
        self.root.suffix_link = self.root
        self.active_node = self.root
        self.active_edge = None
        self.active_length = 0


    def ukkonen(self):
        """
        Applies the Ukkonen's algorithm to construct a suffix tree for the given text.

        The Ukkonen's algorithm is an efficient algorithm for constructing suffix trees. It builds the suffix tree
        incrementally, one character at a time, using a series of tricks to optimize the construction process.

        Time complexity: O(n)(Best case), where n is the length of the input text
                       : O(n^2)(Worst case), where n is the length of the input text
        Space complexity: O(n), where n is the length of the input text
        """
        
        global_end = End(-1)
        global_j = 0
        text = self.text

        for i in range(len(text)):
            # trick 1: rapid leaf extension
            global_end.increment()
            prev_node = None
            
            for j in range(global_j, i+1):   

                if self.active_node == self.root:
                    self.active_length = i-j
            

                self.active_node = self.traverse(text, i, self.active_node, self.active_length)
                edge = self.active_node.get_edge(text[i - self.active_length])
                
                # char is not in the tree
                if edge is None:
                    # trick 2: add branch
                    new_node = Node(i-self.active_length, global_end, is_leaf=True)
                    self.active_node.add_edge(text[i - self.active_length], new_node)
                    new_node.suffix_id = j
                    global_j += 1
                    
                # char is already in the tree
                elif edge is not None: 

                    # char is not in the edge, skip count
                    if self.active_length > edge.get_end() - edge.start:               
                        self.active_node = edge
                        self.active_edge = edge.end
      
                    # char is already exist in the edge
                    elif text[i] == text[edge.start + self.active_length]:
                        self.active_length += 1
                        self.active_edge = self.active_node.get_edge(text[j])

                        # update suffix link
                        if prev_node is not None:
                            prev_node.suffix_link = self.active_node
                        break
                    
                    # char is not in the edge
                    elif text[i] != text[edge.start + self.active_length]:
                        # trick 3: split edge

                        # reset values for original parent node
                        intermediate_node = Node(edge.start, edge.start+self.active_length-1, is_leaf=False)
                        self.active_node.add_edge(text[edge.start], intermediate_node)

                        # branch off new node
                        new_node = Node(i, global_end, is_leaf=True)
                        new_node.suffix_id = j
                        intermediate_node.add_edge(text[global_end.end], new_node) 

                        # reset edge for original node
                        edge.start = edge.start + self.active_length  
                        intermediate_node.add_edge(text[edge.start], edge)

                        # update suffix link
                        intermediate_node.suffix_link = self.root

                        # update suffix link for previous node
                        if prev_node is not None:
                            prev_node.suffix_link = intermediate_node

                        prev_node = intermediate_node

                        # increment global j
                        global_j += 1

                # update active node
                self.active_node = self.active_node.suffix_link
            




    def inorder_traversal(self, node):
        """
        Perform an inorder traversal of the tree starting from the given node.
        
        Time complexity: O(n), where n is the number of nodes in the tree
        Space complexity: O(n), where n is the number of nodes in the tree
        """
        res = []
        def inorder_traversal_helper(node):

            if node.is_leaf:
                res.append(node.suffix_id)
            else:
                for child in node.children:
                    if child is not None:
                        inorder_traversal_helper(child)
        inorder_traversal_helper(node)

        return res

    

    def traverse(self, text, i, active_node, active_length):
        """
        Traverses the suffix tree to find the active node based on the given parameters.
        
        Time complexity: O(n), where n is the number of nodes in the tree
        Space complexity: O(1)
        """

        active_edge = active_node.get_edge(text[i - active_length])
        self.active_length = active_length

        # if reached the leaf, return current node
        if active_node.is_leaf or active_length == 0:
            return active_node
        
        # if active length is greater than the edge length, skip to the next node
        if active_length > active_edge.get_end() - active_edge.start:
            return self.traverse(text, i, active_edge, active_length - (active_edge.get_end() - active_edge.start + 1))
        else:
            return active_node
        
    def write_file(self, res, positions):
        positions = [int(item.strip()) for item in positions]
        with open("output_q1.txt","w") as f:
            for i in positions:
                for j in range(len(res)):
                    if i-1 == res[j]:               # -1 to match the 0-based indexing
                        f.write((j+1).__str__() + "\n")
                        break
            f.close()
        

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

    T = SuffixTree(file1content[0])
    T.ukkonen()
    res = T.inorder_traversal(T.root)
    T.write_file(res, file2content)

    
