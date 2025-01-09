import unittest
from app.app import app
from app.sudopy import Sudoku

class TestSudokuApp(unittest.TestCase):

    def setUp(self):
        # Set up the Flask test client
        self.app = app.test_client()
        self.app.testing = True

    def test_menu_route(self):
        # Test the home '/' route
        response = self.app.get("/")
        self.assertEqual(response.status_code, 200)
        # Check if specific text or HTML elements are present in the rendered page
        self.assertIn(b"Sudoku Menu", response.data, "Menu page should contain 'Sudoku Menu'.")


    def test_solve_method(self):
        # Test the Sudoku 'solve' method
        grid = [
            [5, 3, 0, 0, 7, 0, 0, 0, 0],
            [6, 0, 0, 1, 9, 5, 0, 0, 0],
            [0, 9, 8, 0, 0, 0, 0, 6, 0],
            [8, 0, 0, 0, 6, 0, 0, 0, 3],
            [4, 0, 0, 8, 0, 3, 0, 0, 1],
            [7, 0, 0, 0, 2, 0, 0, 0, 6],
            [0, 6, 0, 0, 0, 0, 2, 8, 0],
            [0, 0, 0, 4, 1, 9, 0, 0, 5],
            [0, 0, 0, 0, 8, 0, 0, 7, 9],
        ]
        sudoku = Sudoku(grid)
        solved = sudoku.solve()
        self.assertTrue(solved, "The Sudoku puzzle should be solvable.")
        self.assertTrue(sudoku.check_grid(), "The solved Sudoku puzzle should be valid.")

if __name__ == "__main__":
    unittest.main()