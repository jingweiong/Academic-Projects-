from collections import deque
from math import inf

"""
Assignment 2

Name: Ong Jing Wei
Student id: 32909764
Last modified: 25/5/2023

This python file consists of two main function:
    1. maxThroughput(connections, maxIn, maxOut, origin, targets)
    2. autoComplete(self, prompt)

"""

def maxThroughput(connections, maxIn, maxOut, origin, targets):
    """
    maxThroughput function takes in all the necessary input and calls the ford fulkerson algorithm
    to get the maximum possible data throughput from origin to targets.

    @author Ong Jing Wei

    Precondition: None
    Postcondition: None

    Input:
    :param connections: A list of tuple (a,b,c) where 'a' is the start vertex and 'b' is the end vertex
                        where it travels from vertex 'a' to vertex 'b', while 'c' is the maximum
                        throughput of the channel
    :param maxIn: A list of integers where maxIn[i] is the maximum incoming data which can enter to vertex i
    :param maxOut: A list of integers where maxOut[i] is the maximum outgoing data which can leave from vertex i
    :param origin: An integer to represent the starting vertex
    :param targets: A list of integers which represent the targets vertex

    Output:
    :return: Maximum possible data throughput

    Time Complexity: O(|D| |C|^2), where D is the data centres and C is the communication channels

    Space Complexity: O(|D| |C|^2), where D is the data centres and C is the communication channels
    """

    def fordfulkerson (connections):
        """
        fordfulkerson is a function which implements the Ford Fulkerson Algorithm and returns the maximum
        throughput where it first creates a residual graph and run the BFS function on the residual graph.
        If there is still a path in the residual graph, it will find the residual capacity of that path and
        add on to the maximum throughput. After get the residual capacity, it will also augment the flow in
        the residual graph.

        @author Ong Jing Wei (referred to lecture video)

        Input:
        :param connections: A list of tuple (a,b,c) where 'a' is the start vertex and 'b' is the end vertex
                            where it travels from vertex 'a' to vertex 'b', while 'c' is the maximum
                            throughput of the channel

        Output:
        :return: Maximum possible data throughput

        Time Complexity: O(|D| |C|^2), where D is the data centres and C is the communication channels

        Space Complexity: O(|D| |C|^2), where D is the data centres and C is the communication channels

        """

        maxthroughput = 0
        residual = ResidualGraph(connections, maxIn, maxOut, origin, targets)

        # check if there still a path in the residual graph
        while residual.bfs():
            # get the path from the bfs
            path = residual.get_AugmentingPath()
            # get the bottleneck value from the path
            bottleneck = residual.residual_capacity(path)
            # add the bottleneck value to the maxthroughput
            maxthroughput += bottleneck
            # augment the path
            residual.augment_flow(path, bottleneck)
            # reset the residual graph
            residual.reset()

        return maxthroughput

    return fordfulkerson(connections)


class ResidualGraph:
    """
    Residual Graph class initialize all the vertex and edges from the input given. It also creates an extra
    edge from the end vertex to the start vertex and initialize the capacity of this edge to 0. This class
    also consist of some functions for the Residual Graph

    @author Ong Jing Wei
    """
    def __init__(self, connections, maxIn, maxOut, origin, targets):

        self.vertices = []
        for i in range(len(maxIn)):
            self.vertices.append(Vertex(i, maxIn[i], maxOut[i]))

        for each in connections:
            u = self.vertices[each[0]]
            v = self.vertices[each[1]]
            capacity = min(v.maxIn, each[2], u.maxOut)
            u.maxOut -= capacity
            v.maxIn -= capacity
            u.add_edges(u, v, capacity)
            v.add_edges(v, u, 0)

        # create a sink to connect all the targets
        if len(targets) > 1:
            self.vertices.append(Vertex(len(maxIn), 0, 0))
            for i in targets:
                self.vertices[i].add_edges(self.vertices[i], self.vertices[-1], max(maxOut[i], maxIn[i]))
                self.vertices[-1].add_edges(self.vertices[-1], self.vertices[i], 0)
            self.sink = self.vertices[-1]
        elif len(targets) == 1:
            self.sink = self.vertices[targets[0]]

        self.source = self.vertices[origin]

    def bfs(self):
        """
        bfs function which will run breath first search through the residual graph and return boolean value
        to indicate if the residual graph still have a path. This function also keep track of the previous
        vertex so that we can backtrack the path.

        @author Ong Jing Wei

        Output:
        :return: boolean value

        Time Complexity: O(V+E) where V is the vertices and E is the edges

        Space Complexity: O(V+E) where V is the vertices and E is the edges

        """
        queue = deque()
        queue.append(self.source)
        while len(queue) > 0:
            u = queue.popleft()
            u.visited = True
            for edge in u.edges:
                if edge.capacity > 0:   # if the edge has a capacity greater than 0
                    v = edge.v
                    if not v.visited and not v.discovered:
                        v.discovered = True
                        queue.append(v)
                        v.previous = u
                    if v.id == self.sink.id:
                        return True
                elif edge.capacity == 0:    # if the capacity is 0 then we skip that edge
                    pass
        return False

    def get_AugmentingPath(self):
        """
        get_AugmentingPath function where it returns the path from the bfs function

        @author Ong Jing Wei

        Output:
        :return: A path from origin to super sink

        Time complexity: O(V) where V is the length of the path

        Space complexity: O(V) where V is the length of the path
        """
        vertex = self.sink
        path = deque([vertex])
        while vertex != self.source:
            path.appendleft(vertex.previous)
            vertex = vertex.previous
        return path

    def residual_capacity(self, path):
        """
        residual_capacity function where it go through the path and get the minimum capacity across the path
        and return the bottleneck

        @author Ong Jing Wei

        Input:
        :param path: Input path

        Output:
        :return: minimum capacity

        Time complexity: O(V) where V is the length of the path

        Space complexity: None
        """
        bottleneck = inf
        i = 0
        while i < len(path) - 1:
            for edge in path[i].edges:
                if edge.v == path[i+1] and edge.capacity < bottleneck:
                    bottleneck = edge.capacity
            i += 1
        return bottleneck

    def augment_flow(self, path, capacity):
        """
        augment_flow function takes in the path and the bottleneck value of the path and update the
        residual graph

        @author Ong Jing Wei

        Input:
        :param path: Input path
        :param capacity: bottleneck capacity of the path

        Time Complexity: O(V) where V is the length of the path

        Space Complexity: None
        """
        for i in range(len(path)-1):
            for edge in path[i].edges:
                if edge.v == path[i+1]:
                    edge.flow += capacity
                    edge.capacity -= capacity
            for edge in path[i+1].edges:
                if edge.v == path[i]:
                    edge.flow -= capacity
                    edge.capacity += capacity

    def reset(self):
        """
        reset function reset all the vertices attributes to default before running bfs

        @author Ong Jing Wei

        Time Complexity: O(V) where V is the total vertices
        """

        for vertex in self.vertices:
            vertex.discovered = False
            vertex.visited = False
            vertex.previous = None


class Vertex:
    """
       Vertex class to initialize the vertices

    """
    def __init__(self, id, maxIn, maxOut):
        """
        Constructor for vertex class

        @author Ong Jing Wei

        :param id: Vertex id
        :param maxIn: maximum incoming data
        :param maxOut: maximum outgoing data
        """
        self.id = id
        self.maxIn = maxIn
        self.maxOut = maxOut

        self.edges = []
        self.visited = False
        self.discovered = False
        self.previous = None

    def add_edges(self, u, v, capacity):
        """
        add_edges function add the respective edges to the vertex

        @author Ong Jing Wei

        Input
        :param u: starting vertex
        :param v: ending vertex
        :param capacity: capacity of the edge between u and v

        Time Complexity: O(1)

        """
        self.edges.append(Edges(u, v, capacity))


class Edges:
    """
    Edge class to initialize the edges
    """
    def __init__(self, u, v, capacity):
        """
        Constructor for edge class

        @author Ong Jing Wei

        Input:
        :param u: starting vertex
        :param v: ending vertex
        :param capacity: capacity between vertex u and v

        """
        self.u = u
        self.v = v
        self.flow = 0
        self.capacity = capacity


class CatsTrie:
    """
    CatsTrie class to initialize the trie
    """

    def __init__(self, sentences):
        """
        Constructor for CatsTrie class

        @author Ong Jing Wei

        Input:
        :param sentences: A list of string
        """
        self.root = Node()
        self.root.data = "root"
        for i in sentences:
            self.insert(i)

    def insert(self, key):
        """
        insert function to insert the node

        @author Ong Jing Wei

        Input:
        :param key: A string

        Time Complexity: O(NM) where N is the number of sentence in sentences
                         and M is the number of characters in the longest sentence

         Space complexity: O(NM) where N is the number of sentence in sentences
                          and M is the number of characters in the longest sentence
        """
        current = self.root
        pointer = 0
        self.insert_recur(current, key, pointer)

    def insert_recur(self, current, key, pointer):
        """
        Recursive function for the insert

        @author Ong Jing Wei

        Input:
        :param current: current node
        :param key: string to be inserted
        :param pointer: a pointer to keep track of the character in the string

        Time complexity: O(NM) where N is the number of sentence in sentences
                         and M is the number of characters in the longest sentence

        Space complexity: O(NM) where N is the number of sentence in sentences
                          and M is the number of characters in the longest sentence
        """
        if len(key) == pointer:
            if current.link[0] is not None:
                current.link[0].is_unique = False
                current.link[0].freq += 1
                return current.link[0]
            else:
                current.link[0] = Node()
                current.link[0].data = "$"
                current.link[0].unique += 1
                current.link[0].is_unique = True
                current.link[0].freq += 1
                if current.data == "root":
                    self.update_best_child(current, current.link[0])
                return current.link[0]
        else:
            index = ord(key[pointer]) - 97 + 1
            if current.link[index] is not None:
                current = current.link[index]
                current.freq += 1
                last = self.insert_recur(current, key, pointer+1)
                if last.is_unique == True:
                    current.unique += 1
                    current.is_unique = True
                else:
                    current.is_unique = False
                self.update_best_child(current, last)

            else:
                current.link[index] = Node()
                child = current.link[index]
                child.data += key[pointer]
                if current.data == "root":
                    self.update_best_child(current, child)
                current = child
                current.freq += 1
                last = self.insert_recur(current, key, pointer+1)
                current.unique += 1
                current.is_unique = True
                self.update_best_child(current, last)

        return current

    def update_best_child(self, current, child):
        """
        update_best_child function compares the current node's best child and the new child and replaced it
        if the new child has a higher frequency than the existing best child

        @author Ong Jing Wei

        Input:
        :param current: current node
        :param child: child node

        Time Complexity: O(1)

        Space Complexity: None
        """
        if current.highest_child == None:
            current.highest_child = child
        else:
            if (child.freq - child.unique) > (current.highest_child.freq - current.highest_child.unique):
                current.highest_child = child
            elif (child.freq - child.unique) == (current.highest_child.freq - current.highest_child.unique):
                if child.data < current.highest_child.data:
                    current.highest_child = child

    def autoComplete(self, prompt):
        """
        autoComplete function takes in prompt and complete it.

        @author Ong Jing Wei

        Input:
        :param prompt: a string

        Output:
        :return: a string which is the completed sentence

        Time complexity: O(X+Y) where X is the length of the prompt and Y is the most frequent sentence

        Space complexity: O(X) where X is the length of the prompt
        """
        current = self.root
        result = ""

        if len(prompt) == 0:
            current = current.highest_child
        else:
            for prm in prompt:
                index = ord(prm) - 97 + 1
                if current.link[index] == None:
                    return None
                else:
                    result += current.link[index].data
                    current = current.link[index]
            current = current.highest_child

        while current.data != "$":
            result += current.data
            current = current.highest_child

        return result


class Node:
    """
    Node class to create a node object and store its attributes

    """
    def __init__(self):
        """
        Constructor for node class

        @author Ong Jing Wei
        """
        self.link = [None] * 27
        self.data = ""
        self.freq = 0
        self.unique = 0
        self.is_unique = False
        self.children = []
        self.highest_child = None


if __name__ == "__main__":
    connections = [(0, 1, 3000), (1, 2, 2000), (1, 3, 1000),
    (0, 3, 2000), (3, 4, 2000), (3, 2, 1000)]
    maxIn = [5000, 3000, 3000, 3000, 2000]
    maxOut = [5000, 3000, 3000, 2500, 1500]
    origin = 0
    targets = [4, 2]

    # connections = [(0, 1, 3000), (1, 2, 2000), (1, 3, 1000), (0, 3, 2000), (3, 4, 2000), (3, 2, 1000)]
    # maxIn = [5000, 3000, 3000, 3000, 2000]
    # maxOut = [5000, 3000, 3000, 2500, 1500]
    # origin = 1
    # targets = [3]

    maxThroughput(connections, maxIn, maxOut, origin, targets)

    # Example 1, similar to the introduction
    # but with some additional sentences
    sentences = ["abc", "abazacy", "dbcef", "xzz", "gdbc", "abazacy", "xyz",
                 "abazacy", "dbcef", "xyz", "xxx", "xzz"]
    trie = CatsTrie(sentences)
    trie.autoComplete("a")

