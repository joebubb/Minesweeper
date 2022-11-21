package model;

import view.MinesweeperGUI;

public class MinesweeperObserverImp implements MinesweeperObserver {
    private MinesweeperGUI gui;

    public MinesweeperObserverImp(MinesweeperGUI gui){
        this.gui = gui;
    }
    @Override
    public void cellUpdated(Location location) throws MinesweeperException {
        gui.updateCell(location);
    }
}
