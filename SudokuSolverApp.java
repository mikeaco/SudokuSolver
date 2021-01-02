import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class SudokuSolverApp extends JFrame {
	SudokuSolver solver = new SudokuSolver();
	boolean finished = false;
	static boolean start = false;
	long SPEED = 500;
	Thread thread;
	public void addComponentsToPane(Container pane) {
		int width = Toolkit.getDefaultToolkit().getScreenSize().width;// gets the screen width
		int height = Toolkit.getDefaultToolkit().getScreenSize().height;// gets the screen height
		JPanel window = new JPanel();
		JPanel grid = new JPanel();
		JPanel[][] boxes = new JPanel[9][9];
		JPanel title = new JPanel();
		JPanel topButtons = new JPanel();
		JButton solve = new JButton("Solve");
		JButton animate = new JButton("Animate");
		JButton check = new JButton("Check");
		JButton reset = new JButton("Reset");
		JButton changeBoard = new JButton("Change Board");
		JLabel message = new JLabel("Welcome");
		JRadioButton[] speed = new JRadioButton[3];
		
		title.add(new JLabel("Sudoku Solver"));
		window.setBackground(Color.white);
		window.setPreferredSize(new Dimension(width / 4, height / 2 + (height / 6)));
		window.setLayout(new FlowLayout());
		title.setPreferredSize(new Dimension(width / 4, height / 30));
		title.setBackground(Color.white);
		topButtons.setBackground(Color.white);
		topButtons.add(check);
		topButtons.add(solve);
		topButtons.add(animate);
		topButtons.add(reset);

		grid.setPreferredSize(new Dimension(width / 4, width / 4 - 11));
		grid.setLayout(new GridLayout(3, 3));
		for (int r = 0; r < 3; r++) {
			for (int c = 0; c < 3; c++) {
				JPanel box = new JPanel();
				box.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
				box.setLayout(new GridLayout(3, 3));
				grid.add(box);
				for (int r1 = 0; r1 < 3; r1++) {
					for (int c1 = 0; c1 < 3; c1++) {
						JPanel box1 = new JPanel();
						boxes[r1 + r * 3][c1 + c * 3] = box1;
						box1.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
						if (solver.getBoard()[r1 + r * 3][c1 + c * 3] == '.') {
							JTextField text = new JTextField(2);
							text.setBorder(null);
							text.addKeyListener(new KeyAdapter() {
								@Override
								public void keyTyped(KeyEvent e) {
									if (text.getText().length() >= 1) // limit to 3 characters
										e.consume();
								}
							});
							box1.add(text);

						} else {
							box1.add(new JLabel("" + solver.getBoard()[r1 + r * 3][c1 + c * 3]));
						}
						box.add(box1);

					}
				}

			}
		}
		class ButtonListener implements ActionListener {

			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand().equals("0")) {
					SPEED = 500;
				}
				if (e.getActionCommand().equals("1")) {
					SPEED = 100;
				}
				if (e.getActionCommand().equals("2")) {
					SPEED = 50;
				}
				if (e.getActionCommand().equals("animate") && !finished && !start) {
					solver.setAnim(new ArrayList<>());
					solver.solveSudoku(solver.getBoard(), solver.getAnim());
					ArrayList<int[]> anim = solver.getAnim();
					
					start = true;
					Runnable runnable = new Runnable() {
						@Override
						public void run() {
							if (start) {

								System.out.println(anim.size());
								for (int i = 0; i < anim.size(); i++) {
									boxes[anim.get(i)[2]][anim.get(i)[3]].removeAll();
									
									switch(anim.get(i)[1]) {
										case 0:
											boxes[anim.get(i)[2]][anim.get(i)[3]].setBorder(BorderFactory.createLineBorder(Color.green, 2));
											boxes[anim.get(i)[2]][anim.get(i)[3]].add(new JLabel("" + anim.get(i)[0]));
											break;
										case 1:
											boxes[anim.get(i)[2]][anim.get(i)[3]].setBorder(BorderFactory.createLineBorder(Color.yellow, 2));
											JLabel l = new JLabel("" + anim.get(i)[0]);
											boxes[anim.get(i)[2]][anim.get(i)[3]].add(l);
											break;
									

									}
									boxes[anim.get(i)[2]][anim.get(i)[3]].revalidate();
									boxes[anim.get(i)[2]][anim.get(i)[3]].repaint();
									try {
										Thread.sleep(SPEED);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

								}
								start = false;
								finished = true;
							}
						}
					};
					thread = new Thread(runnable);
					thread.start();
					

				}
				if (e.getActionCommand().equals("solve") && !finished&&!start) {
					solver.setAnim(new ArrayList<>());
					solver.solveSudoku(solver.getBoard(), solver.getAnim());
					message.setText("Solution");
					solver.printBoard();
					for (int r = 0; r < boxes.length; r++) {
						for (int c = 0; c < boxes.length; c++) {
							
							boxes[r][c].removeAll();
							boxes[r][c].add(new JLabel("" + solver.getBoard()[r][c]));
							boxes[r][c].revalidate();
							boxes[r][c].repaint();

						}
					}
					finished = true;
				}
				if (e.getActionCommand().equals("change")&&!start) {
					finished = false;
					solver.randomBoard();
					message.setText("Welcome");
					for (int r = 0; r < boxes.length; r++) {
						for (int c = 0; c < boxes.length; c++) {
							
							boxes[r][c].removeAll();
							boxes[r][c].setBorder(BorderFactory.createLineBorder(Color.gray, 1));
							if (solver.getBoard()[r][c] != '.') {
								boxes[r][c].add(new JLabel("" + solver.getBoard()[r][c]));
							} else {
								JTextField text = new JTextField(2);
								text.setBorder(null);
								text.addKeyListener(new KeyAdapter() {
									@Override
									public void keyTyped(KeyEvent e) {
										if (text.getText().length() >= 1) // limit to 3 characters
											e.consume();
									}
								});
								boxes[r][c].add(text);
							}
							boxes[r][c].revalidate();
							boxes[r][c].repaint();

						}
					}
				}
				if (e.getActionCommand().equals("reset")&&!start) {
					finished = false;
					solver.resetBoard();
					message.setText("Welcome");
					for (int r = 0; r < boxes.length; r++) {
						for (int c = 0; c < boxes.length; c++) {
							boxes[r][c].removeAll();
							boxes[r][c].setBorder(BorderFactory.createLineBorder(Color.gray, 1));
							if (solver.getBoard()[r][c] != '.') {
								boxes[r][c].add(new JLabel("" + solver.getBoard()[r][c]));
							} else {
								JTextField text = new JTextField(2);
								text.setBorder(null);
								text.addKeyListener(new KeyAdapter() {
									@Override
									public void keyTyped(KeyEvent e) {
										if (text.getText().length() >= 1) // limit to 3 characters
											e.consume();
									}
								});
								boxes[r][c].add(text);
								boxes[r][c].revalidate();
								boxes[r][c].repaint();
							}

						}
					}
				}
				if (e.getActionCommand().equals("check") && !finished&&!start) {
					boolean correct = true;
					boolean all = true;
					char[][] solved = solver.getSolvedBoard();
					for (int r = 0; r < boxes.length; r++) {
						for (int c = 0; c < boxes[r].length; c++) {
							if (boxes[r][c].getComponent(0) instanceof JTextField) {
								if (((JTextField) (boxes[r][c].getComponent(0))).getText().length() > 0) {
									char cur = ((JTextField) (boxes[r][c].getComponent(0))).getText().charAt(0);
									if (cur != solved[r][c]) {
										message.setText("Incorrect");
										((JTextField) (boxes[r][c].getComponent(0)))
												.setBorder(BorderFactory.createLineBorder(Color.red, 2));
										correct = false;
										all = false;
									} else {
										((JTextField) (boxes[r][c].getComponent(0)))
												.setBorder(BorderFactory.createLineBorder(Color.green, 2));
									}
								} else {
									all = false;
								}
							}
						}
					}
					if (correct) {
						message.setText("Looks good so far!");
					}
					if (correct && all) {
						message.setText("Congratulations you solved it!!");
						for (int r = 0; r < boxes.length; r++) {
							for (int c = 0; c < boxes[r].length; c++) {
								if (boxes[r][c].getComponent(0) instanceof JTextField) {
									((JTextField) (boxes[r][c].getComponent(0))).setEditable(false);
								}
							}
						}
						finished = true;
					}
				}
			}

		}
		JPanel speedPanel = new JPanel();
		speedPanel.setBackground(Color.white);
		speedPanel.add(new JLabel("Speed: "));
		ButtonGroup speedButtons = new ButtonGroup();
		speed[0] = new JRadioButton("Slow");
		speed[1] = new JRadioButton("Medium");
		speed[2] = new JRadioButton("Fast");
		speed[0].setSelected(true);
		for(int i = 0 ; i < speed.length ; i++) {
			speed[i].addActionListener(new ButtonListener());
			speed[i].setActionCommand(i+"");
			speed[i].setBackground(Color.white);
			speedButtons.add(speed[i]);
			speedPanel.add(speed[i]);
		}
		solve.setActionCommand("solve");
		solve.addActionListener(new ButtonListener());
		reset.setActionCommand("reset");
		reset.addActionListener(new ButtonListener());
		check.setActionCommand("check");
		check.addActionListener(new ButtonListener());
		changeBoard.setActionCommand("change");
		changeBoard.addActionListener(new ButtonListener());
		animate.setActionCommand("animate");
		animate.addActionListener(new ButtonListener());
		window.add(title);
		window.add(topButtons);
		window.add(message);
		window.add(grid);
		window.add(speedPanel);
		window.add(changeBoard);
		
		pane.add(window);
	}

	private static void createAndShowGUI(SudokuSolverApp view) {
		// Create and set up the window.

		SudokuSolverApp frame = view;

		frame.setTitle("Sudoku Solver");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		// Set up the content pane.
		frame.addComponentsToPane(frame.getContentPane());
		// Display the window.

		frame.pack();
		frame.setVisible(true); // Important!!

	}

	public static void main(String[] args) {

		SudokuSolverApp viewer = new SudokuSolverApp();

		javax.swing.SwingUtilities.invokeLater(new Runnable() { // used in conjunction with threads
			public void run() { // a thread created by a GUI element looks for the 'run' method
				createAndShowGUI(viewer);// makes the window
			}
		});
	}

}
