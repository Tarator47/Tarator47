import random


class Sudoku:
    def __init__(self, matrix):
        """
        Initializes an object of type Sudoku
        with a 2D list containing the digits.

        Parameters:
        matrix (list): a 2d list that the object uses to initialize self.grid
        """
        self.grid = matrix

    def __str__(self):
        """
        Creates and returns a string representation of the Sudoku object.
        """
        string = ''
        for i in range(9):
            for j in range(9):
                string += str(self.grid[i][j]) + " "
            string += "\n"
        return string

    def input_num(self, x, y, num):
        """
        Inputs a number in the specified x, y location in self.grid.

        Parameters:
        x (int): x location where num should be placed
        y (int): y location where num should be placed
        num (int): number between 1-9 that should be inputted in the
            appropriate x, y location in self.grid
        """
        self.grid[x][y] = num

    def return_array(self):
        """
        Returns the grid attribute of a Sudoku object.
        """
        return self.grid

    def create_RCB_lists(self, x, y):
        """
        Creates three separate lists of all
        numbers in a given row, column, and box.

        Parameters:
        x (int): x location that should be checked
        y (int): y location that should be checked

        Returns:
        tuple: three elements in the tuple that correspond to the
            row list, column list, and box list
        """
        list_r = self.grid[x]
        list_c = [self.grid[i][y] for i in range(9)]
        list_b = []
    
        mod_r, mod_c = (x % 3), (y % 3)
        list_mr = [x - mod_r + i for i in range(3) if 0 <= x - mod_r + i < 9]
        list_mc = [y - mod_c + i for i in range(3) if 0 <= y - mod_c + i < 9]
    
        for i in list_mr:
            for j in list_mc:
                list_b.append(self.grid[i][j])
    
        return list_r, list_c, list_b


    def check_num(self, x, y, num):
        """
        Checks if a single number is valid in a given row, column, or box.

        Parameters:
        x (int): x location that should be checked
        y (int): y location that should be checked
        num (int): number between 1-9 that should be checked for validity

        Returns:
        boolean: True if the number is valid in the given location
        """
        if self.grid[x][y] != 0:
            return False

        list_r, list_c, list_b = self.create_RCB_lists(x, y)
        return num not in list_r and num not in list_c and num not in list_b

    def solve(self):
        """
        Recursive function that solves
        a given grid with a backtracking algorithm.

        Returns:
        boolean: True if the solve is valid, False otherwise
        """
        empty_loc = self.locate_empty()
        if not empty_loc:
            return True

        x, y = empty_loc
        for i in range(1, 10):
            if self.check_num(x, y, i):
                self.input_num(x, y, i)
                if self.solve():
                    return True
                self.input_num(x, y, 0)
        return False

    def locate_empty(self):
        """
        Finds a location in self.grid that
        is 0, representing an empty location.

        Returns:
        tuple: the empty location in the grid, or None if the grid is full.
        """
        for x in range(9):
            for y in range(9):
                if self.grid[x][y] == 0:
                    return x, y
        return None


class Generate:
    def __init__(self, difficulty_level):
        """
        Initializes a Generate object with a difficulty_level attribute.

        Parameters:
        difficulty_level (str): specified
        difficulty ("easy", "medium", "hard", "expert")
        """
        self.difficulty_level = difficulty_level

    def generate_sudoku(self):
        """
        Selects a random Sudoku with
        a difficulty based on self.difficulty_level.

        Returns:
        list: a 2D list initialized with
        digits that correspond to a unique sudoku.
        """
        file_name = f'sudokus/sudokus_{self.difficulty_level}.txt'
        with open(file_name, 'r') as file:
            all_lines = file.readlines()

        rand_line = random.choice([line for line in all_lines
                                   if len(line.strip()) > 2])
        return [
            [0 if char == '.' else int(char)
             for char in rand_line[i * 9:(i + 1) * 9]]
            for i in range(9)
        ]
