package avl.boardgame;

import java.util.ArrayList;

public class Board {

	private ArrayList<Cell> cells;

	public Board() {
		super();
		this.cells = new ArrayList<Cell>(32);
		this.initBoard();
	}

	public Cell getCell(int id) {
		return this.cells.get(id);
	}

	public ArrayList<Cell> getCells() {
		return cells;
	}

	public void initBoard() {
		this.cells.add(new Rest());
		this.cells.add(new Investment(4000000, 5));
		this.cells.add(new PublicFinanceOffice(5));
		this.cells.add(new Investment(10, 70));
		this.cells.add(new Investment(540, 10));
		this.cells.add(new AntiTrustLaw());
		this.cells.add(new Investment(730, 7));
		this.cells.add(new Investment(1300, 2));
		this.cells.add(new Rest());
		this.cells.add(new Investment(5340, 10));
		this.cells.add(new Subsidy(1000));
		this.cells.add(new Investment(5340, 10));
		this.cells.add(new Investment(70, 10));
		this.cells.add(new PublicFinanceOffice(1));
		this.cells.add(new Investment(100, 20));
		this.cells.add(new PublicFinanceOffice(10));
		this.cells.add(new Rest());
		this.cells.add(new Investment(1000, 5));
		this.cells.add(new PublicFinanceOffice(15));
		this.cells.add(new Investment(5000, 3));
		this.cells.add(new Investment(100, 20));
		this.cells.add(new PublicFinanceOffice(2));
		this.cells.add(new Investment(4000000, 0.5f));
		this.cells.add(new Rest());
		this.cells.add(new AntiTrustLaw());
		this.cells.add(new Investment(5300, 3));
		this.cells.add(new PublicFinanceOffice(10));
		this.cells.add(new Investment(10000, 1));
		this.cells.add(new PublicFinanceOffice(5));
		this.cells.add(new Investment(730, 7));

	}

}
