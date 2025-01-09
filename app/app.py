from flask import Flask, request, render_template
from sudopy import Sudoku, Generate

app = Flask(__name__)

# The 2D list of the starting numbers in the Sudoku
play_list = []

# Boolean that is True if a Sudoku has been generated using
# the Generate object, False otherwise
has_gen = False


@app.route('/')
def menu():
    global has_gen
    has_gen = False
    return render_template('menu.html')


@app.route('/input_play')
def input_play():
    return render_template('input_play.html')


@app.route('/input_solve')
def input_solve():
    return render_template('input_solve.html')


@app.route('/play', methods=['POST', 'GET'])
def play():
    global play_list
    global has_gen
    sudoku_grid_list = []

    if request.method == 'POST':
        # If the POST request is from '/input_play' or '/play'
        if 'play' in request.referrer:
            # Build 2D list to initialize Sudoku object
            for i in range(9):
                row_list = []
                for j in range(9):
                    cell_id = f"{i * 9 + j}"
                    content = request.form[cell_id]
                    row_list.append(0 if content == "" else int(content))
                sudoku_grid_list.append(row_list)
            s = Sudoku(sudoku_grid_list)

            # If the POST request is from '/input_play'
            if '/input_play' in request.referrer:
                play_list = s.return_array()
                return render_template(
                    'play.html', 
                    is_solved=None, 
                    input_array=play_list, 
                    play_array=None
                )

            # If the POST request is from '/play'
            elif '/play' in request.referrer:
                return render_template(
                    'play.html', 
                    is_solved=s.check_grid(), 
                    check_array=s.check_grid_items(), 
                    input_array=play_list, 
                    play_array=s.return_array()
                )

        # If the POST request is from the menu page
        elif '/' in request.referrer:
            difficulty = (
                request.form.get('easy') or
                request.form.get('medium') or
                request.form.get('hard') or
                request.form.get('expert')
            )

            # Generate Sudoku of the specified difficulty
            if not has_gen:
                g = Generate(difficulty)
                play_list = g.generate_sudoku()
                has_gen = True
            return render_template(
                'play.html', 
                is_solved=None, 
                input_array=play_list, 
                play_array=None
            )
    else:
        return render_template(
            'play.html', 
            is_solved=None, 
            input_array=play_list, 
            play_array=None
        )


@app.route('/solution', methods=['POST', 'GET'])
def solution():
    sudoku_grid_list = []
    if request.method == 'POST':
        # Build 2D list to initialize Sudoku object
        for i in range(9):
            row_list = []
            for j in range(9):
                cell_id = f"{i * 9 + j}"
                content = request.form[cell_id]
                row_list.append(0 if content == "" else int(content))
            sudoku_grid_list.append(row_list)

        # Create the solved Sudoku
        s = Sudoku(sudoku_grid_list)
        s.solve()
        return render_template('solution.html', solved_array=s.return_array())
    else:
        return render_template('menu.html')


if __name__ == "__main__":
    app.run(debug=True, host='0.0.0.0', port=7650)
