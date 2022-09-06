package app;

import Cards.Card;
import gameSpaces.Property;
import gameSpaces.avenueProperty;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class guiBoard extends JFrame {

    public gameSpace[] gameSpaces;
    public boardPanel BOARDPANEL;
    private ClientGame parentGame;
    public playerInfoPanel PLAYERINFOPANEL;
    public controlPanel CONTROLPANEL;
    public JLabel[] playerTokens = new JLabel[4];
    private int tokenWidth = 22, tokenHeight = 22;
    private int houseWidth = 8, houseHeight = 8;
    private int hotelWidth = 11, hotelHeight = 11;
    private ArrayList<ImageIcon> playerPieces = new ArrayList<ImageIcon>();
    public DealPanel dealPanel;
    public JDialog dealPanelPane = new JDialog();

    public JDialog adjustPropertiesPane = new JDialog();
    public JDialog messageDialog = new JDialog();
    public JDialog inJailDialog = new JDialog();

    Color[] ownershipColors = {new Color(255, 230, 10), new Color(0, 123, 255), new Color(34, 139, 34), new Color(215, 0, 64)};
    Color unOwnedColor = new Color(0,0,0);
    Color unMortgagedColor = new Color(246, 246, 246);
    Color mortgagedColor = new Color(114, 114, 114);

    ImageIcon battleshipIcon = new ImageIcon(new ImageIcon(this.getClass().getResource("/assets/monopoly-piece-battleship.png")).getImage().getScaledInstance(tokenWidth, tokenHeight, Image.SCALE_SMOOTH));
    ImageIcon carIcon = new ImageIcon(new ImageIcon(this.getClass().getResource("/assets/monopoly-piece-car.png")).getImage().getScaledInstance(tokenWidth, tokenHeight, Image.SCALE_SMOOTH));
    ImageIcon dogIcon = new ImageIcon(new ImageIcon(this.getClass().getResource("/assets/monopoly-piece-dog.png")).getImage().getScaledInstance(tokenWidth, tokenHeight, Image.SCALE_SMOOTH));
    ImageIcon hatIcon = new ImageIcon(new ImageIcon(this.getClass().getResource("/assets/monopoly-piece-hat.png")).getImage().getScaledInstance(tokenWidth, tokenHeight, Image.SCALE_SMOOTH));

    ImageIcon houseIcon = new ImageIcon(new ImageIcon(this.getClass().getResource("/assets/house.png")).getImage().getScaledInstance(houseWidth, houseHeight, Image.SCALE_SMOOTH));
    ImageIcon hotelIcon = new ImageIcon(new ImageIcon(this.getClass().getResource("/assets/hotel.png")).getImage().getScaledInstance(hotelWidth, hotelHeight, Image.SCALE_SMOOTH));


    public guiBoard(ClientGame client) {

        this.parentGame = client;

        gameSpaces = new gameSpace[40];

        getContentPane().setLayout(new BorderLayout());

        BOARDPANEL = new boardPanel();
        PLAYERINFOPANEL = new playerInfoPanel();
        CONTROLPANEL = new controlPanel();

        add(PLAYERINFOPANEL, BorderLayout.CENTER);
        add(CONTROLPANEL, BorderLayout.SOUTH);

        add(BOARDPANEL, BorderLayout.NORTH);
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);

        this.playerPieces.add(battleshipIcon);
        this.playerPieces.add(carIcon);
        this.playerPieces.add(dogIcon);
        this.playerPieces.add(hatIcon);
    }

    private class gameSpace extends JPanel
    {

        public JLabel spaceTitle;
        public JPanel buildingPanel;
        public ArrayList<JLabel> buildingsArray;

        public gameSpace()
        {
            super(new FlowLayout());
            spaceTitle = new JLabel();
            add(spaceTitle);
        }

    }

    public class playerInfoPanel extends JPanel
    {
        public JLabel moneyLabel;

        private final String moneyPrefix = "Your Money: ";

        public playerInfoPanel()
        {
            super(new BorderLayout());
            moneyLabel = new JLabel();
            add(moneyLabel, BorderLayout.NORTH);

            pack();
            setPreferredSize(new Dimension(725, 50));
            setMinimumSize(new Dimension(725, 50));
            setMaximumSize(new Dimension(725, 50));
        }

        public void changeMoneyAmount(int newAmount)
        {
            moneyLabel.setText(moneyPrefix + newAmount);
        }

    }


    public class controlPanel extends JPanel
    {
        public JLabel serverLabel;
        public JLabel gameLabel;
        public JLabel turnLabel;
        public JButton rollButton;
        public JButton dealButton;

        public controlPanel()
        {
            super(new BorderLayout());
            rollButton = new JButton();
            rollButton.setText("Roll");
            rollButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        parentGame.roll();
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            });

            dealButton = new JButton();
            dealButton.setText("Make a Deal");
            dealButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JOptionPane newPane = new JOptionPane();
                    newPane.setMessageType(JOptionPane.PLAIN_MESSAGE);
                    dealPanel = new DealPanel(parentGame.getDealingPlayer());
                    newPane.setMessage(dealPanel);
                    dealPanelPane = newPane.createDialog(BOARDPANEL, "Let's Make a Deal ...");
                    dealPanelPane.setVisible(true);
                }
            });

            JPanel buttonsPanel = new JPanel(new GridLayout());
            buttonsPanel.add(rollButton);
            buttonsPanel.add(dealButton);
            add(buttonsPanel, BorderLayout.NORTH);

            serverLabel = new JLabel("");
            gameLabel = new JLabel("");
            turnLabel = new JLabel("");
            JPanel labelPanel = new JPanel(new BorderLayout());
            labelPanel.add(turnLabel, BorderLayout.NORTH);
            labelPanel.add(gameLabel, BorderLayout.CENTER);
            labelPanel.add(serverLabel, BorderLayout.SOUTH);
            add(labelPanel, BorderLayout.SOUTH);

            pack();
            setPreferredSize(new Dimension(725, 90));
            setMinimumSize(new Dimension(725, 90));
            setMaximumSize(new Dimension(725, 90));
        }

        public void updateServerMessage(String message)
        {
            serverLabel.setText(message);
        }

        public void updateGameMessage(String message)
        {
            gameLabel.setText(message);
        }

        public void updateTurnLabel(String message)
        {
            turnLabel.setText(message);
        }

    }

    private class boardPanel extends JPanel
    {

        int panelWidth = 725, panelHeight = 725;

        public boardPanel()
        {
            super();
            //Setup the Layout
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            GridBagLayout thisLayout = new GridBagLayout();
            thisLayout.rowWeights = new double[] { 2, 1.1, 1.1, 1.1, 1.1, 1.1, 1.1, 1.1, 1.1, 1.1, 2 };
            thisLayout.columnWeights = new double[] { 2, 1.1, 1.1, 1.1, 1.1, 1.1, 1.1, 1.1, 1.1, 1.1, 2 };
            setLayout(thisLayout);

            //Default Grid values
            int gridX = 0;
            int gridY = 0;
            //Add Panels for Each of the four sides
            for (int j = 0; j < 4; j++) {
                for (int i = 0; i <= 9; i++) {
                    gameSpace tempPanel = new gameSpace();
                    double helper = 1.1;
                    if (i == 0 || i == 10)
                    {
                        helper = 2;
                    }
                    int width = (int) ((helper * panelWidth) / 13.9);
                    int height = (int) ((helper * panelHeight) / 13.9);
                    tempPanel.setPreferredSize(new Dimension(width, height));
                    tempPanel.setMinimumSize(new Dimension(width, height));
                    tempPanel.setMaximumSize(new Dimension(width, height));

                    switch(j)
                    {
                        case 2://Top Spaces
                            gridX = i;
                            gridY = 0;
                            break;
                        case 1://Left Spaces
                            gridX = 0;
                            gridY = (10 - i);
                            break;
                        case 3://Right Spaces
                            gridX = 10;
                            gridY = i;
                            break;
                        case 0://Bottom Spaces
                            gridX = (10 - i);
                            gridY = 10;
                            break;
                    }
                    add(tempPanel,
                            new GridBagConstraints(gridX,// XGridSpot
                                    gridY,// YGridSpot
                                    1,// XGridSpaces
                                    1,// YGridSpaces
                                    0.0, 0.0, GridBagConstraints.CENTER,
                                    GridBagConstraints.BOTH,//Fill
                                    new Insets(0, 0, 0, 0), 0, 0));
                    tempPanel.setBorder(BorderFactory.createLineBorder(unOwnedColor));
                    tempPanel.setBackground(unMortgagedColor);
                    int position = (j * 10) + i;
                    gameSpaces[position] = tempPanel;
                }
            }

            {// Main Inner Area Notice Starts at (1,1) and takes up 11x11
                JPanel innerPanel = new JPanel();
                add(
                        innerPanel,
                        new GridBagConstraints(1,
                                1,
                                9,
                                9,
                                0.0, 0.0,
                                GridBagConstraints.CENTER,
                                GridBagConstraints.BOTH,
                                new Insets(0, 0, 0, 0), 0, 0));
            }
            pack();
            setPreferredSize(new Dimension(panelWidth, panelHeight));
            setMinimumSize(new Dimension(panelWidth, panelHeight));
            setMaximumSize(new Dimension(panelWidth, panelHeight));
        }

    }

    public void initializeSpaces()
    {
        Board gameBoard = this.parentGame.getBoard();
        for (int counter = 0; counter < gameSpaces.length; counter++)
        {
            gameSpaces[counter].spaceTitle.setText(gameBoard.boardSpaces[counter].spaceName);

            if (gameBoard.boardSpaces[counter] instanceof avenueProperty)
            {
                gameSpaces[counter].buildingPanel = new JPanel();
                gameSpaces[counter].buildingPanel.setOpaque(false);
                gameSpaces[counter].buildingPanel.setPreferredSize(new Dimension(gameSpaces[counter].getWidth(), 10));
                gameSpaces[counter].add(gameSpaces[counter].buildingPanel);

                gameSpaces[counter].buildingsArray = new ArrayList<>();
            }

            if (gameBoard.boardSpaces[counter] instanceof Property) {
                int finalCounter = counter;
                gameSpaces[counter].addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        JOptionPane newPane = new JOptionPane();
                        if (parentGame.canAdjustProperty((Property) gameBoard.boardSpaces[finalCounter]))
                        {
                            newPane.setMessageType(JOptionPane.PLAIN_MESSAGE);
                            newPane.setMessage(new propertyDecisionPanel((Property) gameBoard.boardSpaces[finalCounter]));
                            adjustPropertiesPane = newPane.createDialog(BOARDPANEL, "Adjusting ...");
                            adjustPropertiesPane.setVisible(true);
                        }
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {

                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {

                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {

                    }

                    @Override
                    public void mouseExited(MouseEvent e) {

                    }
                });
            }
        }
    }

    public void addPlayerPiece(Player player)
    {
        JPanel thisPanel = gameSpaces[player.position];

        JLabel tokenLabel = new JLabel();
        tokenLabel.setIcon(playerPieces.get(player.playerNumber - 1));
        tokenLabel.setSize(new Dimension(tokenWidth, tokenHeight));
        tokenLabel.setPreferredSize(new Dimension(tokenWidth, tokenHeight));
        tokenLabel.setMaximumSize(new Dimension(tokenWidth, tokenHeight));

        thisPanel.add(tokenLabel);
        thisPanel.revalidate();
        thisPanel.repaint();
        revalidate();

        playerTokens[player.playerNumber - 1] = tokenLabel;
    }

    public void movePlayerPiece(Player player)
    {
        int oldPosition = player.previousPosition;
        JPanel oldSpace = gameSpaces[oldPosition];
        JLabel tokenLabel = playerTokens[player.playerNumber - 1];
        oldSpace.remove(tokenLabel);
        oldSpace.revalidate();
        oldSpace.repaint();
        revalidate();

        JPanel newSpace = gameSpaces[player.position];
        newSpace.add(tokenLabel);
        newSpace.revalidate();
        newSpace.repaint();
        revalidate();
    }

    public void guiJailAsker(boolean canRoll)
    {
        if (inJailDialog != null)
        {
            inJailDialog.dispose();
            inJailDialog.setVisible(false);
        }
        inJailDialog = new JDialog();
        JOptionPane newPane = new JOptionPane("", JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
        newPane.setMessage(new jailDecisionPanel(canRoll));
        inJailDialog = newPane.createDialog(BOARDPANEL, "In Jail");
        inJailDialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        inJailDialog.setVisible(true);
    }

    private class jailDecisionPanel extends JPanel {
        public JButton rollButton;
        public JButton payBailButton;
        public JButton useGetOutOfJailFreeButton;


        public jailDecisionPanel(boolean canRoll)
        {
            super(new BorderLayout());
            rollButton = new JButton();
            rollButton.setText("Roll");
            if (!canRoll)
            {
                rollButton.setEnabled(false);
            }
            rollButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        parentGame.roll();
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    inJailDialog.dispose();
                    inJailDialog.repaint();
                }
            });

            payBailButton = new JButton();
            payBailButton.setText("Pay Bail");
            if (!parentGame.canPayNow(parentGame.getBailAmount()))
            {
                payBailButton.setEnabled(false);
            }
            payBailButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    parentGame.payBail();
                    inJailDialog.dispose();
                }
            });

            useGetOutOfJailFreeButton = new JButton();
            useGetOutOfJailFreeButton.setText("Use GOOJFC");
            if (!parentGame.canUseGetOutOfJailFreeCard())
            {
                useGetOutOfJailFreeButton.setEnabled(false);
            }
            useGetOutOfJailFreeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    parentGame.useGetOutOfJailFreeCard();
                    inJailDialog.dispose();
                }
            });

            if (parentGame.canUseGetOutOfJailFreeCard())
            {
                add(rollButton, BorderLayout.WEST);
                add(payBailButton, BorderLayout.CENTER);
                add(useGetOutOfJailFreeButton, BorderLayout.EAST);
            }
            else
            {
                add(rollButton, BorderLayout.WEST);
                add(payBailButton, BorderLayout.EAST);
            }

        }

    }

    public void toggleButtons(boolean offOn)
    {
        CONTROLPANEL.rollButton.setEnabled(offOn);
    }

    public boolean askToBuy(Property property)
    {
        int decision = JOptionPane.showConfirmDialog(getContentPane(), "Do you want to buy " + property.spaceName + " for $" + property.initialCost + "?", "Buy Option", JOptionPane.YES_NO_OPTION);
        if (decision == 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public void changeOwnership(Property propertyToChange)
    {
        int position = propertyToChange.spacePosition;
        Player newOwner = propertyToChange.ownedBy;
        Color borderColor = null;
        if (newOwner != null) {
            borderColor = ownershipColors[newOwner.playerNumber - 1];
        }
        else
        {
            borderColor = unOwnedColor;
        }

        gameSpaces[position].setBorder(BorderFactory.createLineBorder(borderColor));
    }

    public void mortgageGui(int spaceNumber)
    {
        gameSpaces[spaceNumber].setBackground(mortgagedColor);
        gameSpaces[spaceNumber].revalidate();
        gameSpaces[spaceNumber].repaint();
    }

    public void unMortgageGui(int spaceNumber)
    {
        gameSpaces[spaceNumber].setBackground(unMortgagedColor);
        gameSpaces[spaceNumber].revalidate();
        gameSpaces[spaceNumber].repaint();
    }

    public void raiseGui(avenueProperty avenue)
    {
        int spaceNumber = avenue.spacePosition;
        JLabel newLabel = new JLabel();
        ImageIcon buildingIcon = new ImageIcon();
        if (avenue.buildingLevel > 4)
        {
            for (int x = (gameSpaces[spaceNumber].buildingsArray.size() - 1); x >= 0; x--)
            {
                //Remove four houses first before putting up hotel
                gameSpaces[spaceNumber].buildingPanel.remove(gameSpaces[spaceNumber].buildingsArray.get(x));
                gameSpaces[spaceNumber].buildingsArray.remove(x);
            }
            buildingIcon = hotelIcon;
        }
        else
        {
            buildingIcon = houseIcon;
        }
        newLabel.setIcon(buildingIcon);
        gameSpaces[spaceNumber].buildingsArray.add(newLabel);
        gameSpaces[spaceNumber].buildingPanel.add(gameSpaces[spaceNumber].buildingsArray.get(gameSpaces[spaceNumber].buildingsArray.size() - 1));
        gameSpaces[spaceNumber].buildingPanel.revalidate();
        gameSpaces[spaceNumber].buildingPanel.repaint();
    }

    public interface CallbackJList {
        void call(int selectedIndex);
    }

    public class DealPanel extends JPanel
    {
        private JPanel yourSide;
        private JPanel theirSide;
        private DefaultListModel propertiesToDeal = new DefaultListModel();
        private DefaultListModel theirPropertiesToDeal = new DefaultListModel();
        private Player dealingPlayer;
        private Player partnerPlayer;
        private JButton proposeButton;
        private JButton acceptButton;
        private ArrayList<Property> dealingProperties = new ArrayList<>();
        private ArrayList<Property> theirDealingProperties = new ArrayList<>();
        private JTextField moneyDeal = new JTextField();
        private JTextField theirMoneyDeal = new JTextField();
        private JList playerOptions;
        private JList yourProperties;
        private JList theirProperties;
        private JList dealProperties;
        private JList theirDealProperties;

        private class theMouse extends MouseAdapter
        {
            private JList list;
            private CallbackJList callback;

            public theMouse(JList list, CallbackJList callback)
            {
                this.list = list;
                this.callback = callback;
            }

            public void mouseClicked(MouseEvent e)
            {
                if (e.getClickCount() == 2 && list.getSelectedIndex() > -1) {
                    int selectedIndex = list.getSelectedIndex();
                    callback.call(selectedIndex);
                }
            }

        }

        private DefaultListModel getPlayerProperties(Player player)
        {
            DefaultListModel propertiesModel = new DefaultListModel();
            for (Property property : player.properties)
            {
                propertiesModel.addElement(property.spaceName);
            }
            return propertiesModel;
        }

        private void resetDeal()
        {
            propertiesToDeal = new DefaultListModel();
            theirPropertiesToDeal = new DefaultListModel();
            dealingProperties = new ArrayList<>();
            theirDealingProperties = new ArrayList<>();
            yourProperties.setModel(new DefaultListModel());
            theirProperties.setModel(new DefaultListModel());
            dealProperties.setModel(new DefaultListModel());
            theirDealProperties.setModel(new DefaultListModel());
        }

        private void addToDeal(JList list, boolean usOrThem, Property property)
        {
            if (usOrThem)
            {
                if (dealingProperties.contains(property))
                {
                    return;
                }
                dealingProperties.add(property);
                propertiesToDeal.addElement(property.spaceName);
                list.setModel(propertiesToDeal);
            }
            else
            {
                if (theirDealingProperties.contains(property))
                {
                    return;
                }
                theirDealingProperties.add(property);
                theirPropertiesToDeal.addElement(property.spaceName);
                list.setModel(theirPropertiesToDeal);
            }
        }

        private void removeFromDeal(JList list, boolean usOrThem, int index)
        {
            if (usOrThem)
            {
                propertiesToDeal.removeElementAt(index);
                list.setModel(propertiesToDeal);
                dealingProperties.remove(index);
            }
            else
            {
                theirPropertiesToDeal.removeElementAt(index);
                list.setModel(theirPropertiesToDeal);
                theirDealingProperties.remove(index);
            }
        }

        private void checkForValidDeal()
        {
            if (parentGame.isDealValid(dealingPlayer, partnerPlayer, moneyDeal.getText(), theirMoneyDeal.getText(), dealingProperties, theirDealingProperties))
            {
                proposeButton.setEnabled(true);
            }
            else
            {
                proposeButton.setEnabled(false);
            }
        }

        private void setScrollBar(JList list, int y, JPanel parent)
        {
            JScrollPane scrollPane = new JScrollPane();
            scrollPane.setViewportView(list);
            list.setLayoutOrientation(JList.VERTICAL);
            parent.add(scrollPane, new GridBagConstraints(0,// XGridSpot
                    y,// YGridSpot
                    2,// XGridSpaces
                    1,// YGridSpaces
                    1.0, 1.0, GridBagConstraints.CENTER,
                    GridBagConstraints.BOTH,//Fill
                    new Insets(0, 0, 18, 0), 0, 0));

        }

        public void enableAcceptance()
        {
            acceptButton.setEnabled(true);
        }

        public DealPanel(Player dealPlayer)
        {
            super(new BorderLayout());
            yourSide = new JPanel(new GridBagLayout());
            theirSide = new JPanel(new GridBagLayout());
            dealingPlayer = dealPlayer;

            JLabel nameLabel = new JLabel(dealingPlayer.playerName + " (you):");
            JLabel moneyLabel = new JLabel("Money: $" + dealingPlayer.readyCash);
            JLabel moneyDealLabel = new JLabel("Deal Money:");
            JLabel theirMoneyDealLabel = new JLabel("Deal Money:");
            moneyDeal = new JTextField("0");
            theirMoneyDeal = new JTextField("0");
            moneyDeal.setPreferredSize(new Dimension(72, 15));
            theirMoneyDeal.setPreferredSize(new Dimension(72, 15));
            JLabel theirMoney = new JLabel("Money: $");
            JLabel dealWith = new JLabel("Deal with...");
            ArrayList<Player> possiblePartners = parentGame.getPossibleDealingPlayers();
            final DefaultListModel playersModel = new DefaultListModel();
            for (Player player : possiblePartners)
            {
                playersModel.addElement(player.playerName);
            }
            playerOptions = new JList(playersModel);
            yourProperties = new JList(getPlayerProperties(dealingPlayer));
            theirProperties = new JList(new DefaultListModel());
            dealProperties = new JList(propertiesToDeal);
            theirDealProperties = new JList(theirPropertiesToDeal);
            setScrollBar(yourProperties, 3, yourSide);
            setScrollBar(dealProperties, 4, yourSide);
            setScrollBar(theirProperties, 4, theirSide);
            setScrollBar(theirDealProperties, 5, theirSide);

            proposeButton = new JButton("Propose");
            acceptButton = new JButton("Accept");
            proposeButton.setEnabled(false);
            acceptButton.setEnabled(false);

            JPanel buttonsPanel = new JPanel(new BorderLayout());
            buttonsPanel.add(proposeButton, BorderLayout.NORTH);
            buttonsPanel.add(acceptButton, BorderLayout.SOUTH);

            moneyDeal.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    checkForValidDeal();
                }
            });

            theirMoneyDeal.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    checkForValidDeal();
                }
            });

            proposeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    proposeButton.setEnabled(false);
                    parentGame.proposeDeal(dealingPlayer, partnerPlayer, moneyDeal.getText(), theirMoneyDeal.getText(), dealingProperties, theirDealingProperties);
                }
            });

            acceptButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    parentGame.acceptDeal(dealingPlayer, partnerPlayer, moneyDeal.getText(), theirMoneyDeal.getText(), dealingProperties, theirDealingProperties);
                    dealPanelPane.setVisible(false);
                    dealPanelPane.dispose();
                    dealPanelPane = new JDialog();
                }
            });

            playerOptions.addMouseListener(new theMouse(playerOptions, new CallbackJList() {
                public void call(int index) {
                    resetDeal();
                    Player partner = possiblePartners.get(index);
                    partnerPlayer = partner;
                    theirMoney.setText("Money: $" + partner.readyCash);
                    yourProperties.setModel(getPlayerProperties(dealingPlayer));
                    theirProperties.setModel(getPlayerProperties(partner));
                    checkForValidDeal();
                }
            }));

            yourProperties.addMouseListener(new theMouse(yourProperties, new CallbackJList() {
                public void call(int index) {
                    addToDeal(dealProperties,true, dealingPlayer.properties.get(index));
                    checkForValidDeal();
                }
            }));

            theirProperties.addMouseListener(new theMouse(theirProperties, new CallbackJList() {
                public void call(int index) {
                    addToDeal(theirDealProperties,false, partnerPlayer.properties.get(index));
                    checkForValidDeal();
                }
            }));

            dealProperties.addMouseListener(new theMouse(dealProperties, new CallbackJList() {
                public void call(int index) {
                    removeFromDeal(dealProperties, true, index);
                    checkForValidDeal();
                }
            }));

            theirDealProperties.addMouseListener(new theMouse(theirDealProperties, new CallbackJList() {
                public void call(int index) {
                    removeFromDeal(theirDealProperties, false, index);
                    checkForValidDeal();
                }
            }));

            yourSide.add(nameLabel);
            yourSide.add(moneyLabel, new GridBagConstraints(0,// XGridSpot
                    1,// YGridSpot
                    2,// XGridSpaces
                    1,// YGridSpaces
                    1.0, 1.0, GridBagConstraints.CENTER,
                    GridBagConstraints.BOTH,//Fill
                    new Insets(0, 0, 18, 0), 0, 0));
            yourSide.add(moneyDealLabel, new GridBagConstraints(0,// XGridSpot
                    2,// YGridSpot
                    1,// XGridSpaces
                    1,// YGridSpaces
                    1.0, 1.0, GridBagConstraints.CENTER,
                    GridBagConstraints.BOTH,//Fill
                    new Insets(0, 0, 18, 0), 0, 0));
            yourSide.add(moneyDeal, new GridBagConstraints(1,// XGridSpot
                    2,// YGridSpot
                    2,// XGridSpaces
                    1,// YGridSpaces
                    3.0, 1.0, GridBagConstraints.CENTER,
                    GridBagConstraints.BOTH,//Fill
                    new Insets(0, 0, 18, 0), 0, 0));
            theirSide.add(dealWith);
            theirSide.add(playerOptions, new GridBagConstraints(0,// XGridSpot
                    1,// YGridSpot
                    2,// XGridSpaces
                    1,// YGridSpaces
                    1.0, 1.0, GridBagConstraints.CENTER,
                    GridBagConstraints.BOTH,//Fill
                    new Insets(0, 0, 18, 0), 0, 0));
            theirSide.add(theirMoney, new GridBagConstraints(0,// XGridSpot
                    2,// YGridSpot
                    2,// XGridSpaces
                    1,// YGridSpaces
                    1.0, 1.0, GridBagConstraints.CENTER,
                    GridBagConstraints.BOTH,//Fill
                    new Insets(0, 0, 18, 0), 0, 0));
            theirSide.add(theirMoneyDealLabel, new GridBagConstraints(0,// XGridSpot
                    3,// YGridSpot
                    1,// XGridSpaces
                    1,// YGridSpaces
                    1.0, 1.0, GridBagConstraints.CENTER,
                    GridBagConstraints.BOTH,//Fill
                    new Insets(0, 0, 18, 0), 0, 0));
            theirSide.add(theirMoneyDeal, new GridBagConstraints(1,// XGridSpot
                    3,// YGridSpot
                    2,// XGridSpaces
                    1,// YGridSpaces
                    3.0, 1.0, GridBagConstraints.CENTER,
                    GridBagConstraints.BOTH,//Fill
                    new Insets(0, 0, 18, 0), 0, 0));


            JPanel topPanel = new JPanel(new GridBagLayout());
            topPanel.add(yourSide, new GridBagConstraints(0,// XGridSpot
                    0,// YGridSpot
                    1,// XGridSpaces
                    1,// YGridSpaces
                    1.0, 1.0, GridBagConstraints.CENTER,
                    GridBagConstraints.BOTH,//Fill
                    new Insets(0, 0, 18, 0), 0, 0));
            topPanel.add(theirSide, new GridBagConstraints(1,// XGridSpot
                    0,// YGridSpot
                    1,// XGridSpaces
                    1,// YGridSpaces
                    1.0, 1.0, GridBagConstraints.CENTER,
                    GridBagConstraints.BOTH,//Fill
                    new Insets(0, 0, 18, 0), 0, 0));

            add(topPanel, BorderLayout.NORTH);

            add(buttonsPanel, BorderLayout.SOUTH);

            setPreferredSize(new Dimension(450, 550));
        }
    }

    private class propertyDecisionPanel extends JPanel
    {
        public JButton mortgageButton;
        public JButton raiseSingleButton;
        public JButton takeDownSingleButton;
        public JButton raiseSetButton;
        public JButton takeDownSetButton;
        public JLabel propertyTitle;

        private Property property;

        public propertyDecisionPanel(Property prop) {
            super(new GridBagLayout());
            this.property = prop;

            propertyTitle = new JLabel(property.spaceName);
            propertyTitle.setFont(new Font("Cursive", Font.PLAIN, 22));

            mortgageButton = new JButton();
            mortgageEnabler();

            mortgageButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!property.isMortgaged) {
                        mortgageGui(property.spacePosition);
                        parentGame.mortgageProperty(property);
                    } else {
                        unMortgageGui(property.spacePosition);
                        parentGame.unMortgageProperty(property);
                    }
                    allEnabler();
                }
            });

            add(propertyTitle, new GridBagConstraints(0,// XGridSpot
                    0,// YGridSpot
                    2,// XGridSpaces
                    1,// YGridSpaces
                    1.0, 1.0, GridBagConstraints.CENTER,
                    GridBagConstraints.BOTH,//Fill
                    new Insets(0, 0, 18, 0), 0, 0));
            add(mortgageButton, new GridBagConstraints(0,// XGridSpot
                    1,// YGridSpot
                    2,// XGridSpaces
                    1,// YGridSpaces
                    1.0, 1.0, GridBagConstraints.CENTER,
                    GridBagConstraints.BOTH,//Fill
                    new Insets(0, 0, 0, 0), 0, 0));

            if (property instanceof avenueProperty) {
                raiseSingleButton = new JButton();
                raiseSingleButton.setText("Raise on Property");
                raiseSingleButton.setEnabled(false);

                raiseSingleButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        parentGame.raiseOnProperty(property);
                        allEnabler();
                    }
                });


                raiseSetButton = new JButton();
                raiseSetButton.setText("Raise on Set");
                raiseSetEnabler();

                raiseSetButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        parentGame.raiseOnSet(property);
                        allEnabler();
                    }
                });

                takeDownSingleButton = new JButton();
                takeDownSingleButton.setText("Take Down On Property");
                takeDownSingleEnabler();

                takeDownSingleButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        parentGame.takeDownOnProperty(property);
                        allEnabler();
                    }
                });


                takeDownSetButton = new JButton();
                takeDownSetButton.setText("Take Down On Set");
                takeDownSetEnabler();

                takeDownSetButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        parentGame.takeDownSet(property);
                        allEnabler();
                    }
                });



            add(raiseSingleButton, new GridBagConstraints(0,// XGridSpot
                    2,// YGridSpot
                    1,// XGridSpaces
                    1,// YGridSpaces
                    1.0, 1.0, GridBagConstraints.CENTER,
                    GridBagConstraints.BOTH,//Fill
                    new Insets(4, 0, 0, 1), 0, 0));
            add(takeDownSingleButton, new GridBagConstraints(1,// XGridSpot
                    2,// YGridSpot
                    1,// XGridSpaces
                    1,// YGridSpaces
                    1.0, 1.0, GridBagConstraints.CENTER,
                    GridBagConstraints.BOTH,//Fill
                    new Insets(4, 1, 0, 0), 0, 0));

            add(raiseSetButton, new GridBagConstraints(0,// XGridSpot
                    3,// YGridSpot
                    1,// XGridSpaces
                    1,// YGridSpaces
                    1.0, 1.0, GridBagConstraints.CENTER,
                    GridBagConstraints.BOTH,//Fill
                    new Insets(4, 0, 0, 1), 0, 0));
            add(takeDownSetButton, new GridBagConstraints(1,// XGridSpot
                    3,// YGridSpot
                    1,// XGridSpaces
                    1,// YGridSpaces
                    1.0, 1.0, GridBagConstraints.CENTER,
                    GridBagConstraints.BOTH,//Fill
                    new Insets(4, 1, 0, 0), 0, 0));
        }

                allEnabler();
                setPreferredSize(new Dimension(300, 325));
            }


        public void allEnabler()
        {
            mortgageEnabler();
            if (property instanceof avenueProperty)
            {
                raiseSingleEnabler();
                raiseSetEnabler();
                takeDownSingleEnabler();
                takeDownSetEnabler();
            }
        }

        public void mortgageEnabler()
        {
            if (property.isMortgaged)
            {
                mortgageButton.setText("Un-Mortgage Property");
                if (parentGame.canUnMortgage(property))
                {
                    mortgageButton.setEnabled(true);
                }
                else
                {
                    mortgageButton.setEnabled(false);
                }
            }
            else
            {
                mortgageButton.setText("Mortgage Property");
                if (parentGame.canMortgage(property))
                {
                    mortgageButton.setEnabled(true);
                }
                else
                {
                    mortgageButton.setEnabled(false);
                }
            }
        }

        public void raiseSingleEnabler()
        {
            if (parentGame.canRaiseSingle((avenueProperty) property))
            {
                raiseSingleButton.setEnabled(true);
            }
            else
            {
                raiseSingleButton.setEnabled(false);
            }
        }

        public void raiseSetEnabler()
        {
            if (parentGame.canRaiseSet((avenueProperty) property))
            {
                raiseSetButton.setEnabled(true);
            }
            else
            {
                raiseSetButton.setEnabled(false);
            }
        }

        public void takeDownSingleEnabler()
        {
            if (parentGame.canTakeDownSingle((avenueProperty) property))
            {
                takeDownSingleButton.setEnabled(true);
            }
            else
            {
                takeDownSingleButton.setEnabled(false);
            }
        }

        public void takeDownSetEnabler()
        {
            if (parentGame.canTakeDownSet((avenueProperty) property))
            {
                takeDownSetButton.setEnabled(true);
            }
            else
            {
                takeDownSetButton.setEnabled(false);
            }
        }
        }

    public void takeDownGui(avenueProperty avenue)
    {
        int spaceNumber = avenue.spacePosition;
        if (avenue.buildingLevel == 4)
        {
            //Remove hotel first before putting up four houses
            gameSpaces[spaceNumber].buildingPanel.remove(gameSpaces[spaceNumber].buildingsArray.get(0));
            gameSpaces[spaceNumber].buildingsArray.remove(0);
            for (int x = 0; x < 4; x++)
            {
                JLabel newLabel = new JLabel();
                newLabel.setIcon(houseIcon);
                gameSpaces[spaceNumber].buildingsArray.add(x, newLabel);
                gameSpaces[spaceNumber].buildingPanel.add(gameSpaces[spaceNumber].buildingsArray.get(x));
            }
        }
        else
        {
            gameSpaces[spaceNumber].buildingPanel.remove(gameSpaces[spaceNumber].buildingsArray.get(gameSpaces[spaceNumber].buildingsArray.size() - 1));
            gameSpaces[spaceNumber].buildingsArray.remove(gameSpaces[spaceNumber].buildingsArray.size() - 1);
        }
        gameSpaces[spaceNumber].buildingPanel.revalidate();
        gameSpaces[spaceNumber].buildingPanel.repaint();
    }

    public void removeAllBuildingsGui(avenueProperty avenue)
    {
        int spaceNumber = avenue.spacePosition;
        gameSpaces[spaceNumber].buildingPanel.removeAll();
        gameSpaces[spaceNumber].buildingsArray = new ArrayList<>();
        gameSpaces[spaceNumber].buildingPanel.revalidate();
        gameSpaces[spaceNumber].buildingPanel.repaint();
    }

    public void showRentExchange(Property property, Player payer, Player payee, int rent)
    {
        String title = "Time to Get that Rent, Right!";
        String message = payer.playerName + " pays " + payee.playerName + " $" + rent + " for landing on " + property.spaceName + ".";
        createMessageDialog("I'll take the Penthouse!", title, message);
    }

    public void showBankruptcy(Player bankruptPLayer, Player bankruptingPlayer)
    {
        String title = "I guess, those are the breaks of Monopoly.";
        String name = "";
        if (bankruptingPlayer == null)
        {
            name = "the Bank";
        }
        else
        {
            name = bankruptingPlayer.playerName;
        }
        String message = bankruptPLayer.playerName + " has gone bankrupt to " + name + ".";
        createMessageDialog("That's a Shame...", title, message);
    }

    public void showEndGame(Player winner)
    {
        String title = "And The Winner Is ...";
        String message = "Congratulations to " + winner.playerName + " for winning this epic game of Monopoly!!!";
        createMessageDialog("What a Thrill!", title, message);
    }

    public void showDrawnCard(Card.cardDeckType deck, String message)
    {
        String title = "";
        if (deck == Card.cardDeckType.COMMUNITYCHEST)
        {
            title = "Community Chest";
        }
        else if (deck == Card.cardDeckType.CHANCE)
        {
            title = "Chance";
        }
        createMessageDialog("Cards are Exciting", title, message);
    }

    public void createMessageDialog(String dialogTitle, String title, String message)
    {
        messageDialog = new JDialog();
        JOptionPane newPane = new JOptionPane("", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
        newPane.setMessage(new messagePanel(title, message));
        messageDialog = newPane.createDialog(BOARDPANEL, dialogTitle);
        messageDialog.setVisible(true);
    }


    private class messagePanel extends JPanel
    {

        public messagePanel(String title, String message)
        {
            super(new BorderLayout());
            JLabel cardTitle = new JLabel(title);

            JLabel cardMessage = new JLabel(message);

            JButton newButton = new JButton();
            newButton.setText("Got It!");
            newButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    messageDialog.dispose();
                }
            });

            add(cardTitle, BorderLayout.NORTH);
            add(cardMessage, BorderLayout.CENTER);
            add(newButton, BorderLayout.SOUTH);
        }

    }

}