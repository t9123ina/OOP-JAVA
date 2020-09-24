class OXOController
{
    private OXOModel gamemodel;

    public OXOController(OXOModel model)
    {
        this.gamemodel = model;
        OXOPlayer player1 = gamemodel.getPlayerByNumber(0);

        // Set the first player to start the game
        gamemodel.setCurrentPlayer(player1);

    }

    public void handleIncomingCommand(String command) throws InvalidCellIdentifierException, CellAlreadyTakenException, CellDoesNotExistException
    {
        if(gamemodel.getWinner()== null && !gamemodel.isGameDrawn())
        {
            // Handling an invalid input
            if(command.length()!=2) { throw new InvalidCellIdentifierException(command,command); }

            char [] textarr = command.toCharArray();
            final int rows= gamemodel.getNumberOfRows(),columns = gamemodel.getNumberOfColumns();
            final int rowNum = textarr[0]-'a';
            final int colNum = Character.getNumericValue(textarr[1]-1);

            // Handling an invalid cell
            if(!Character.isLetter(textarr[0]) || !Character.isDigit(textarr[1]))
            {
                if(!Character.isLetter(textarr[0])) { throw new InvalidCellIdentifierException(command,textarr[0]); }
                else { throw new InvalidCellIdentifierException(command,textarr[1]); }
            }

            // Handling an invalid row or column
            if(rowNum>=rows || colNum>=columns) { throw new CellDoesNotExistException(rowNum+1,colNum+1); }

            // Handling the cell that has been taken
            if(gamemodel.getCellOwner(rowNum,colNum)!=null) { throw new CellAlreadyTakenException(rowNum+1,colNum+1); }

            setCurrentState(rowNum,colNum, rows, columns);
        }

    }

    private void setCurrentState(int rowNum,int colNum,int rows,int columns)
    {

        // set cell owner, and check winner
        OXOPlayer currentplayer= gamemodel.getCurrentPlayer();
        gamemodel.setCellOwner(rowNum,colNum,currentplayer);
        if(checkWinner(rowNum, colNum, currentplayer)) { gamemodel.setWinner(currentplayer); }

        // check game drawn, and set next player
        int countownercell = checkOwnercell();
        if(countownercell == rows*columns) { gamemodel.setGameDrawn(); }
        OXOPlayer nextplayer= gamemodel.getPlayerByNumber(countownercell%gamemodel.getNumberOfPlayers());
        gamemodel.setCurrentPlayer(nextplayer);

    }

    private boolean checkWinner(int rowNum,int colNum,OXOPlayer player)
    {
        // for row
        int count=0;
        for(int i=0;i<gamemodel.getNumberOfRows();i++) {
            if(gamemodel.getCellOwner(i,colNum)==player) { count++; }
            if(count==gamemodel.getWinThreshold()) { return true; }
        }

        // for column
        count=0;
        for(int i=0;i<gamemodel.getNumberOfColumns();i++) {
            if(gamemodel.getCellOwner(rowNum,i)==player) { count++; }
            if(count==gamemodel.getWinThreshold()) { return true; }
        }

        // for backslash && slash
        int countforbackslash=0;
        int countforslash=0;
        for(int j=0;j<gamemodel.getNumberOfRows();j++){
            for(int i=0;i<gamemodel.getNumberOfColumns();i++)
            {
                // for backslash
                if(j==i&&gamemodel.getCellOwner(j,i)==player) { countforbackslash++; }
                if(countforbackslash==gamemodel.getWinThreshold()) { return true; }
                // for slash
                if(j+i==2&&gamemodel.getCellOwner(j,i)==player) { countforslash++; }
                if(countforslash==gamemodel.getWinThreshold()) { return true; }
            }
        }
        return false;
    }

    private int checkOwnercell(){
        int countownercell=0;
        for(int i=0;i<gamemodel.getNumberOfRows();i++)
        {
            for(int j=0;j<gamemodel.getNumberOfColumns();j++)
            {
                if(gamemodel.getCellOwner(i,j)!=null) { countownercell++; }
            }
        }
        return countownercell;
    }

}
