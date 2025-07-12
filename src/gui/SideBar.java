package gui;

import game.Game;
import game.GameRunner;
import game.Player;
import lib.GraphPaperLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class SideBar extends JPanel {

    public final static int INTERVAL = 50;
    private final GameWindow display;
    private final Font font = new Font("Arial", Font.BOLD, 16);
    private final ComponentList rollPanel = new ComponentList();
    private final ComponentList mainPanel = new ComponentList();
    private final ComponentList buyPanel = new ComponentList();
    private final ComponentList tradePanel = new ComponentList();
    private final ComponentList choosePlayerPanel = new ComponentList();
    private final ComponentList errorPanel = new ComponentList();
    private final ComponentList devPanel = new ComponentList();
    private final ComponentList resPanel = new ComponentList();
    private String resSelection;
    private boolean done = false;
    private final ComponentList stealPanel = new ComponentList();
    private final ComponentList placePanel = new ComponentList();
    private final ComponentList cancelPlacePanel = new ComponentList();
    private final ComponentList setupPanel = new ComponentList();
    private final ComponentList inputResourcesPanel = new ComponentList();
    private ArrayList<String> inputResources = new ArrayList<>();
    private ArrayList<String> offerResources = new ArrayList<>();
    private ArrayList<String> sellResources = new ArrayList<>();
    private Player tradeChoice;
    //private final ExecutorService pool;
    private boolean IRPDone = true;
    private boolean secondRound = false;
    private int count = 0;
    private final KComponent currentPlayerBox;
    // For tracking where we are in turn; 0 = main panel or roll, 1 = trade panel, 2 = buy panel, 3 = dev card panel
    private int flag = 0;
    private Timer timer;

    public SideBar(final GameWindow display) {
        setBackground(new Color(255, 255, 255, 255));
        //pool = Executors.newSingleThreadExecutor();

        this.display = display;
        this.setLayout(new GraphPaperLayout(new Dimension(14, 24)));

        // Current player title (always in sidebar)
        //-------------------------------------------------------------------

        currentPlayerBox = new KComponent(new JLabel(""), new Rectangle(2, 0, 10, 1));
        currentPlayerBox.getComponent().setFont(font);
        currentPlayerBox.getComponent().setForeground(Color.WHITE);
        setCurrentPlayer(GameRunner.getCurrentPlayer());
        add(currentPlayerBox.getComponent(), currentPlayerBox.getRectangle());

        // Roll panel:
        //-------------------------------------------------------------------

        JButton roll = new JButton(new AbstractAction() {
            public void actionPerformed(ActionEvent a) {
                Game g = display.getBoard().getGame();

                if (g.over()) {
                    GameRunner.setWinner(g.winningPlayer());

                    winPanel();
                    return;
                }

                int roll = g.roll();

                if (roll != 7) {
                    mainPanel();
                } else {

                    int remove = 0;
                    if (GameRunner.getNumbPlayers() == 3) {
                        if (GameRunner.getPlayer(0).getTotalResources() > 7)
                            remove = GameRunner.getPlayer(0).getTotalResources() / 2;
                        inputResourcesPanel(remove, GameRunner.getPlayer(0), "Remove " + remove + " resources", false);
                        timer = new Timer(INTERVAL,
                                evt -> {
                                    if (IRPDone) {
                                        timer.stop();
                                        GameRunner.getPlayer(0).removeResources(inputResources);
                                        int remove1 = 0;
                                        if (GameRunner.getPlayer(1).getTotalResources() > 7)
                                            remove1 = GameRunner.getPlayer(1).getTotalResources() / 2;
                                        inputResourcesPanel(remove1, GameRunner.getPlayer(1), "Remove " + remove1 + " resources", false);
                                        timer = new Timer(INTERVAL,
                                                evt1 -> {
                                                    if (IRPDone) {
                                                        timer.stop();
                                                        GameRunner.getPlayer(1).removeResources(inputResources);
                                                        int remove2 = 0;
                                                        if (GameRunner.getPlayer(2).getTotalResources() > 7)
                                                            remove2 = GameRunner.getPlayer(2).getTotalResources() / 2;
                                                        inputResourcesPanel(remove2, GameRunner.getPlayer(2), "Remove " + remove2 + " resources", false);
                                                        timer = new Timer(INTERVAL,
                                                                evt2 -> {
                                                                    if (IRPDone) {
                                                                        timer.stop();
                                                                        GameRunner.getPlayer(2).removeResources(inputResources);
                                                                        display.getBoard().placeRobber();
                                                                        placePanel("Move the robber...");
                                                                        timer = new Timer(INTERVAL,
                                                                                evt3 -> {
                                                                                    if (display.getBoard().getState() != 1) {
                                                                                        timer.stop();
                                                                                        stealPanel();
                                                                                    }
                                                                                });
                                                                        timer.start();
                                                                    }
                                                                });
                                                        timer.start();
                                                    }
                                                });
                                        timer.start();
                                    }
                                });
                    } else {
                        //System.out.println("3");
                        if (GameRunner.getPlayer(0).getTotalResources() > 7)
                            remove = GameRunner.getPlayer(0).getTotalResources() / 2;
                        inputResourcesPanel(remove, GameRunner.getPlayer(0), "Remove " + remove + " resources", false);
                        timer = new Timer(INTERVAL,
                                evt -> {
                                    if (IRPDone) {
                                        timer.stop();
                                        GameRunner.getPlayer(0).removeResources(inputResources);
                                        int remove3 = 0;
                                        if (GameRunner.getPlayer(1).getTotalResources() > 7)
                                            remove3 = GameRunner.getPlayer(1).getTotalResources() / 2;
                                        inputResourcesPanel(remove3, GameRunner.getPlayer(1), "Remove " + remove3 + " resources", false);
                                        timer = new Timer(INTERVAL,
                                                evt4 -> {
                                                    if (IRPDone) {
                                                        timer.stop();
                                                        GameRunner.getPlayer(1).removeResources(inputResources);
                                                        int remove4 = 0;
                                                        if (GameRunner.getPlayer(2).getTotalResources() > 7)
                                                            remove4 = GameRunner.getPlayer(2).getTotalResources() / 2;
                                                        inputResourcesPanel(remove4, GameRunner.getPlayer(2), "Remove " + remove4 + " resources", false);
                                                        timer = new Timer(INTERVAL,
                                                                new ActionListener() {
                                                                    public void actionPerformed(ActionEvent evt4) {
                                                                        if (IRPDone) {
                                                                            timer.stop();
                                                                            GameRunner.getPlayer(2).removeResources(inputResources);
                                                                            int remove4 = 0;
                                                                            if (GameRunner.getPlayer(3).getTotalResources() > 7)
                                                                                remove4 = GameRunner.getPlayer(3).getTotalResources() / 2;
                                                                            inputResourcesPanel(remove4, GameRunner.getPlayer(3), "Remove " + remove4 + " resources", false);
                                                                            timer = new Timer(INTERVAL,
                                                                                    new ActionListener() {
                                                                                        public void actionPerformed(ActionEvent evt4) {
                                                                                            if (IRPDone) {
                                                                                                timer.stop();
                                                                                                GameRunner.getPlayer(3).removeResources(inputResources);
                                                                                                display.getBoard().placeRobber();
                                                                                                placePanel("Move the robber...");
                                                                                                timer = new Timer(INTERVAL,
                                                                                                        new ActionListener() {
                                                                                                            public void actionPerformed(ActionEvent evt4) {
                                                                                                                if (display.getBoard().getState() != 1) {
                                                                                                                    timer.stop();
                                                                                                                    stealPanel();
                                                                                                                }
                                                                                                            }
                                                                                                        });
                                                                                                timer.start();
                                                                                            }
                                                                                        }
                                                                                    });
                                                                            timer.start();
                                                                        }
                                                                    }
                                                                });
                                                        timer.start();
                                                    }
                                                });
                                        timer.start();
                                    }
                                });
                    }
                    timer.start();
                }

                JLabel rollNumb = new JLabel("Roll value: " + roll);
                rollNumb.setFont(font);
                add(rollNumb, new Rectangle(2, 2, 10, 1));
                repaint();
                validate();
            }
        });
        roll.setText("roll the dice");
        rollPanel.add(new KComponent(roll, new Rectangle(3, 5, 8, 3)));

        // Main panel:
        //-------------------------------------------------------------------

        JButton buy = new JButton(new AbstractAction() {
            public void actionPerformed(ActionEvent a) {
                buyPanel();
            }
        });
        buy.setText("buy");
        mainPanel.add(new KComponent(buy, new Rectangle(3, 5, 8, 3)));

        JButton trade = new JButton(new AbstractAction() {
            public void actionPerformed(ActionEvent a) {
                tradePanel();
            }
        });
        trade.setText("trade");
        mainPanel.add(new KComponent(trade, new Rectangle(3, 9, 8, 3)));

        JButton endTurn = new JButton(new AbstractAction() {
            public void actionPerformed(ActionEvent a) {
                Game g = display.getBoard().getGame();

                if (g.over()) {
                    GameRunner.setWinner(g.winningPlayer());

                    winPanel();
                    return;
                }

                GameRunner.nextPlayer();
                rollPanel();
            }
        });
        endTurn.setText("end your turn");
        mainPanel.add(new KComponent(endTurn, new Rectangle(3, 18, 8, 3)));

        // Trade panel:
        //-------------------------------------------------------------------

        JLabel tradeText = new JLabel("Trade with...");
        tradePanel.add(new KComponent(tradeText, new Rectangle(4, 4, 6, 2)));

        // Trade with players
        JButton tradePlayer = new JButton(new AbstractAction() {
            public void actionPerformed(ActionEvent a) {
                choosePlayerPanel();
            }
        });
        tradePlayer.setText("a player");
        tradePanel.add(new KComponent(tradePlayer, new Rectangle(1, 6, 6, 2)));

        // Trade with stock
        JButton tradeStock = new JButton(new AbstractAction() {
            public void actionPerformed(ActionEvent a) {
                resPanel();
                timer = new Timer(INTERVAL,
                        evt -> {
                            if (done) {
                                timer.stop();
                                done = false;
                                final String res = resSelection;
                                inputResourcesPanel(-1, GameRunner.getCurrentPlayer(), "Sell resources", false);
                                timer = new Timer(INTERVAL,
                                        evt5 -> {
                                            if (IRPDone) {
                                                timer.stop();
                                                display.getBoard().getGame().npcTrade(GameRunner.getCurrentPlayer(), res, inputResources);
                                                mainPanel();
                                            }
                                        });
                                timer.start();
                            }
                        });
                timer.start();
            }
        });
        tradeStock.setText("the stock");
        tradePanel.add(new KComponent(tradeStock, new Rectangle(7, 6, 6, 2)));

        // Return to the main panel
        JButton returnMain = new JButton(new AbstractAction() {
            public void actionPerformed(ActionEvent a) {
                mainPanel();
            }
        });
        returnMain.setText("return to main panel");
        tradePanel.add(new KComponent(returnMain, new Rectangle(3, 11, 8, 2)));

        // Buy panel:
        //-------------------------------------------------------------------

        JLabel buyText = new JLabel("Buy a...");
        buyPanel.add(new KComponent(buyText, new Rectangle(4, 4, 6, 2)));

        // Buy settlement
        JButton buySettlement = new JButton(new AbstractAction() {
            public void actionPerformed(ActionEvent a) {
                Game g = display.getBoard().getGame();

                int bought = g.canBuySettlement(GameRunner.getCurrentPlayer());

                if (bought == 0) {
                    display.getBoard().placeSettlement(1);
                    placeCancelPanel("Place a settlement...");
                    timer = new Timer(INTERVAL,
                            evt -> {
                                if (display.getBoard().getState() != 2) {
                                    buyPanel();
                                    timer.stop();
                                }
                            });
                    timer.start();
                } else if (bought == 1) {
                    errorPanel("Insufficient resources!");
                } else if (bought == 2) {
                    errorPanel("Structure capacity reached!");
                }
            }
        });
        buySettlement.setText("settlement");
        buyPanel.add(new KComponent(buySettlement, new Rectangle(1, 6, 6, 2)));

        // Buy city
        JButton buyCity = new JButton(new AbstractAction() {
            public void actionPerformed(ActionEvent a) {
                Game g = display.getBoard().getGame();

                int bought = g.canBuyCity(GameRunner.getCurrentPlayer());

                if (bought == 0) {
                    display.getBoard().placeCity(1);
                    placeCancelPanel("Select a settlement...");
                    timer = new Timer(INTERVAL,
                            evt -> {
                                if (display.getBoard().getState() != 4) {
                                    buyPanel();
                                    timer.stop();
                                }
                            });
                    timer.start();
                } else if (bought == 1) {
                    errorPanel("Insufficient resources!");
                } else if (bought == 2) {
                    errorPanel("Structure capacity reached!");
                }
            }
        });
        buyCity.setText("city");
        buyPanel.add(new KComponent(buyCity, new Rectangle(7, 6, 6, 2)));

        // Buy road
        JButton buyRoad = new JButton(new AbstractAction() {
            public void actionPerformed(ActionEvent a) {
                Game g = display.getBoard().getGame();

                int bought = g.canBuyRoad(GameRunner.getCurrentPlayer());

                if (bought == 0) {
                    display.getBoard().placeRoad(1);
                    placeCancelPanel("Place a road...");
                    timer = new Timer(INTERVAL,
                            evt -> {
                                if (display.getBoard().getState() != 3) {
                                    buyPanel();
                                    timer.stop();
                                }
                            });
                    timer.start();
                } else if (bought == 1) {
                    errorPanel("Insufficient resources!");
                } else if (bought == 2) {
                    errorPanel("Structure capacity reached!");
                }
            }
        });
        buyRoad.setText("road");
        buyPanel.add(new KComponent(buyRoad, new Rectangle(1, 8, 6, 2)));

        // Return to the main panel
        buyPanel.add(new KComponent(returnMain, new Rectangle(3, 12, 8, 2)));

        // Error panel:
        //-------------------------------------------------------------------

        JLabel message = new JLabel("");
        message.setFont(font);
        errorPanel.add(message, new Rectangle(2, 4, 10, 1));

        JButton accept = new JButton(new AbstractAction() {
            public void actionPerformed(ActionEvent a) {
                switch (flag) {
                    case 1:
                        setPanel(tradePanel);
                        break;
                    case 2:
                        setPanel(buyPanel);
                        break;
                    case 3:
                        setPanel(devPanel);
                        break;
                    default:
                        setPanel(mainPanel);
                        break;
                }
            }
        });
        accept.setText("continue");
        errorPanel.add(accept, new Rectangle(3, 7, 9, 2));


        // Return to the main panel
        devPanel.add(new KComponent(returnMain, new Rectangle(3, 12, 8, 2)));

        // Steal panel:
        //-------------------------------------------------------------------

        JComboBox<Player> playerStealBox = new JComboBox<>();
        playerStealBox.setAction(new AbstractAction() {
            public void actionPerformed(ActionEvent a) {
                JComboBox<Player> cb = (JComboBox<Player>) a.getSource();
                Player playerSteal = (Player) cb.getSelectedItem();
                display.getBoard().getGame().takeCard(GameRunner.getCurrentPlayer(), playerSteal);
                mainPanel();
            }
        });

        stealPanel.add(playerStealBox, new Rectangle(3, 6, 8, 2));

        // Choose Trade Partner Panel:
        //-------------------------------------------------------------------

        JComboBox<Player> playerTradeBox = new JComboBox<>();
        playerTradeBox.setAction(new AbstractAction() {
            public void actionPerformed(ActionEvent a) {
                //System.out.println("action!");
                JComboBox<Player> cb = (JComboBox<Player>) a.getSource();
                tradeChoice = (Player) cb.getSelectedItem();
                //System.out.println(tradeChoice);
                inputResourcesPanel(-1, GameRunner.getCurrentPlayer(), "Offer resources", false);
                timer = new Timer(INTERVAL,
                        evt -> {
                            if (IRPDone) {
                                timer.stop();
                                offerResources = inputResources;
                                inputResourcesPanel(-1, tradeChoice, "Sell resources", false);
                                timer = new Timer(INTERVAL,
                                        evt6 -> {
                                            if (IRPDone) {
                                                timer.stop();
                                                sellResources = inputResources;
                                                display.getBoard().getGame().playerTrade(GameRunner.getCurrentPlayer(), tradeChoice, offerResources, sellResources);
                                                mainPanel();
                                            }
                                        });
                                timer.start();
                            }
                        });
                timer.start();
            }
        });

        choosePlayerPanel.add(playerTradeBox, new Rectangle(3, 6, 8, 2));

        // Resource selection panel:
        //-------------------------------------------------------------------

        JButton wool = new JButton(new AbstractAction() {
            public void actionPerformed(ActionEvent a) {
                resSelection = "WOOL";
                done = true;
            }
        });
        wool.setText("wool");
        resPanel.add(wool, new Rectangle(4, 6, 6, 2));

        JButton grain = new JButton(new AbstractAction() {
            public void actionPerformed(ActionEvent a) {
                resSelection = "GRAIN";
                done = true;
            }
        });
        grain.setText("grain");
        resPanel.add(grain, new Rectangle(4, 8, 6, 2));

        JButton lumber = new JButton(new AbstractAction() {
            public void actionPerformed(ActionEvent a) {
                resSelection = "LUMBER";
                done = true;
            }
        });
        lumber.setText("lumber");
        resPanel.add(lumber, new Rectangle(4, 10, 6, 2));

        JButton ore = new JButton(new AbstractAction() {
            public void actionPerformed(ActionEvent a) {
                resSelection = "ORE";
                done = true;
            }
        });
        ore.setText("ore");
        resPanel.add(ore, new Rectangle(4, 12, 6, 2));

        JButton brick = new JButton(new AbstractAction() {
            public void actionPerformed(ActionEvent a) {
                resSelection = "BRICK";
                done = true;
            }
        });
        brick.setText("brick");
        resPanel.add(brick, new Rectangle(4, 14, 6, 2));

        // Place panel
        //-------------------------------------------------------------------

        JLabel mess = new JLabel();
        mess.setFont(font);
        placePanel.add(mess, new Rectangle(2, 8, 10, 4));

        // Cancellable Place Panel

        JLabel mess2 = new JLabel();
        mess2.setFont(font);
        cancelPlacePanel.add(mess2, new Rectangle(2, 8, 10, 4));

        JButton cancel = new JButton(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buyPanel();
                display.getBoard().setState(0);
            }
        });
        cancel.setText("Cancel");
        cancelPlacePanel.add(new KComponent(cancel, new Rectangle(4, 6, 6, 3)));

        // Setup panel
        //-------------------------------------------------------------------

        final JLabel start = new JLabel("Your setup, " + GameRunner.getCurrentPlayer());
        start.setFont(font);
        setupPanel.add(new KComponent(start, new Rectangle(2, 3, 10, 2)));

        JButton begin = new JButton(new AbstractAction() {
            public void actionPerformed(ActionEvent a) {
                //System.out.println(count);
                if (!secondRound) {

                    if (count == GameRunner.getNumbPlayers() - 1) {

                        secondRound = true;
                        count++;
                        //Place capitol commandblock
                        display.getBoard().placeSettlementNoRoad(1);
                        placePanel("Place first settlement..");

                        timer = new Timer(INTERVAL,
                                evt -> {
                                    if (display.getBoard().getState() != 5) {
                                        timer.stop();
                                        //Place Road commandblock
                                        display.getBoard().placeRoad(1);
                                        placePanel("Place a road...");
                                        timer = new Timer(INTERVAL,
                                                evt7 -> {
                                                    if (display.getBoard().getState() != 3) {
                                                        timer.stop();
                                                        setCurrentPlayer(GameRunner.getCurrentPlayer());
                                                        start.setText("Place a settlement, " + GameRunner.getCurrentPlayer());
                                                        setupPanel();
                                                    }
                                                });
                                        timer.start();
                                        //
                                    }
                                });
                        timer.start();
                        //
                    } else {
                        count++;
                        //Place SettlementNoRoad commandblock
                        display.getBoard().placeSettlementNoRoad(1);
                        placePanel("Place first settlement...");
                        timer = new Timer(INTERVAL,
                                evt -> {
                                    if (display.getBoard().getState() != 5) {
                                        timer.stop();
                                        //Place Road commandblock
                                        display.getBoard().placeRoad(1);
                                        placePanel("Place a road...");
                                        timer = new Timer(INTERVAL,
                                                evt8 -> {
                                                    if (display.getBoard().getState() != 3) {
                                                        timer.stop();
                                                        GameRunner.nextPlayer();
                                                        setCurrentPlayer(GameRunner.getCurrentPlayer());
                                                        start.setText("Place a settlement, " + GameRunner.getCurrentPlayer());
                                                        setupPanel();
                                                    }
                                                });
                                        timer.start();
                                        //
                                    }
                                });
                        timer.start();
                        //
                    }
                } else {

                    if (count == 1) {
                        //Place capitol commandblock
                        display.getBoard().placeCapitol();
                        placePanel("Place your capitol...");
                        timer = new Timer(INTERVAL,
                                evt -> {
                                    if (display.getBoard().getState() != 5) {
                                        timer.stop();
                                        //Place Road commandblock
                                        display.getBoard().placeRoad(1);
                                        placePanel("Place a road...");
                                        timer = new Timer(INTERVAL,
                                                evt9 -> {
                                                    if (display.getBoard().getState() != 3) {
                                                        timer.stop();
                                                        //Collections.shuffle(GameRunner.players);
                                                        setCurrentPlayer(GameRunner.getCurrentPlayer());
                                                        rollPanel();
                                                    }
                                                });
                                        timer.start();
                                        //
                                    }
                                });
                        timer.start();
                        //
                    } else {
                        count--;
                        //Place capitol commandblock
                        display.getBoard().placeCapitol();
                        placePanel("Place your capitol...");
                        timer = new Timer(INTERVAL,
                                evt -> {
                                    if (display.getBoard().getState() != 5) {
                                        timer.stop();
                                        //Place Road commandblock
                                        display.getBoard().placeRoad(1);
                                        placePanel("Place a road...");
                                        timer = new Timer(INTERVAL,
                                                evt10 -> {
                                                    if (display.getBoard().getState() != 3) {
                                                        timer.stop();
                                                        GameRunner.prevPlayer();
                                                        setCurrentPlayer(GameRunner.getCurrentPlayer());
                                                        start.setText("Place your capitol, " + GameRunner.getCurrentPlayer());
                                                        setupPanel();
                                                    }
                                                });
                                        timer.start();
                                        //
                                    }
                                });
                        timer.start();
                        //
                    }
                }
            }
        });
        begin.setText("place");
        setupPanel.add(new KComponent(begin, new Rectangle(4, 6, 6, 3)));

        // Input Resources Panel
        //-------------------------------------------------------------------

        JComboBox<Integer> brickCombo = new JComboBox<>();
        brickCombo.setAction(new AbstractAction() {
            public void actionPerformed(ActionEvent a) {
            }
        });

        inputResourcesPanel.add(brickCombo, new Rectangle(2, 6, 3, 1));

        JComboBox<Integer> woolCombo = new JComboBox<>();
        woolCombo.setAction(new AbstractAction() {
            public void actionPerformed(ActionEvent a) {
            }
        });

        inputResourcesPanel.add(woolCombo, new Rectangle(6, 6, 3, 1));

        JComboBox<Integer> oreCombo = new JComboBox<>();
        oreCombo.setAction(new AbstractAction() {
            public void actionPerformed(ActionEvent a) {
            }
        });

        inputResourcesPanel.add(oreCombo, new Rectangle(10, 6, 3, 1));

        JComboBox<Integer> grainCombo = new JComboBox<>();
        grainCombo.setAction(new AbstractAction() {
            public void actionPerformed(ActionEvent a) {
            }
        });

        inputResourcesPanel.add(grainCombo, new Rectangle(4, 10, 3, 1));

        JComboBox<Integer> lumberCombo = new JComboBox<>();
        lumberCombo.setAction(new AbstractAction() {
            public void actionPerformed(ActionEvent a) {
            }
        });

        inputResourcesPanel.add(lumberCombo, new Rectangle(8, 10, 3, 1));

        JLabel playerLabel = new JLabel("Player: ");
        start.setFont(font);
        inputResourcesPanel.add(new KComponent(playerLabel, new Rectangle(2, 3, 15, 1)));

        JButton submitResources = new JButton();
        submitResources.setText("Submit");
        inputResourcesPanel.add(submitResources, new Rectangle(3, 15, 9, 2));

        JLabel brickLabel = new JLabel("Brick");
        start.setFont(font);
        inputResourcesPanel.add(new KComponent(brickLabel, new Rectangle(2, 5, 4, 1)));

        JLabel woolLabel = new JLabel("Wool");
        start.setFont(font);
        inputResourcesPanel.add(new KComponent(woolLabel, new Rectangle(6, 5, 4, 1)));

        JLabel oreLabel = new JLabel("Ore");
        start.setFont(font);
        inputResourcesPanel.add(new KComponent(oreLabel, new Rectangle(10, 5, 4, 1)));

        JLabel grainLabel = new JLabel("Grain");
        start.setFont(font);
        inputResourcesPanel.add(new KComponent(grainLabel, new Rectangle(4, 9, 4, 1)));

        JLabel lumberLabel = new JLabel("Lumber");
        start.setFont(font);
        inputResourcesPanel.add(new KComponent(lumberLabel, new Rectangle(8, 9, 4, 1)));

        //inputResourcesPanel(1, GameRunner.getCurrentPlayer(), "test");

        //-------------------------------------------------------------------

        setupPanel();
    }

    private void setPanel(ComponentList cL) {
        this.removeAll();
        this.add(currentPlayerBox.getComponent(), currentPlayerBox.getRectangle());

        for (KComponent kComponent : cL) {
            this.add(kComponent.getComponent(), kComponent.getRectangle());
        }

        repaint();
        validate();
    }

    public void buyPanel() {
        setPanel(buyPanel);
        flag = 2;
    }

    public void tradePanel() {
        setPanel(tradePanel);
        flag = 1;
    }

    public void rollPanel() {
        setCurrentPlayer(GameRunner.getCurrentPlayer());
        setPanel(rollPanel);
        flag = 0;
    }

    public void mainPanel() {
        setPanel(mainPanel);
        flag = 0;
    }

    public void errorPanel(String str) {
        ((JLabel) errorPanel.get(0).getComponent()).setText(str);
        setPanel(errorPanel);
    }


    public void resPanel() {
        setPanel(resPanel);
    }

    public void stealPanel() {
        //JComboBox<Player> newBox = new JComboBox<Player>();
        AbstractAction action = (AbstractAction) ((JComboBox<Player>) stealPanel.get(0).getComponent()).getAction();
        ((JComboBox<Player>) stealPanel.get(0).getComponent()).setAction(new AbstractAction() {
            public void actionPerformed(ActionEvent a) {

            }
        });
        ((JComboBox<Player>) stealPanel.get(0).getComponent()).removeAllItems();
        for (int i = 0; i < display.getBoard().getGame().getBoard().getRobberAdjacentPlayers().size(); i++) {
            if (!display.getBoard().getGame().getBoard().getRobberAdjacentPlayers().get(i).equals(GameRunner.getCurrentPlayer())) {
                ((JComboBox<Player>) stealPanel.get(0).getComponent()).addItem(display.getBoard().getGame().getBoard().getRobberAdjacentPlayers().get(i));
            }
        }

        ((JComboBox<Player>) stealPanel.get(0).getComponent()).setAction(action);

        if (((JComboBox<Player>) stealPanel.get(0).getComponent()).getItemCount() <= 0) {
            //System.out.println("IF");
            errorPanel("No one to steal from");
        } else {
            //System.out.println("ELSE");
            setPanel(stealPanel);
        }
    }

    public void choosePlayerPanel() {
        AbstractAction action = (AbstractAction) ((JComboBox<Player>) choosePlayerPanel.get(0).getComponent()).getAction();
        // Remove action, so that removeAllItems() does not trigger an event
        ((JComboBox<Player>) choosePlayerPanel.get(0).getComponent()).setAction(new AbstractAction() {
            public void actionPerformed(ActionEvent a) {

            }
        });
        // Depopulate combo box
        ((JComboBox<Player>) choosePlayerPanel.get(0).getComponent()).removeAllItems();
        // Populate combo box
        for (int i = 0; i < GameRunner.getNumbPlayers(); i++) {
            if (!GameRunner.getPlayer(i).equals(GameRunner.getCurrentPlayer())) {
                ((JComboBox<Player>) choosePlayerPanel.get(0).getComponent()).addItem(GameRunner.getPlayer(i));
            }
        }
        // Reset action
        ((JComboBox<Player>) choosePlayerPanel.get(0).getComponent()).setAction(action);
        setPanel(choosePlayerPanel);
    }

    public void placePanel(String str) {
        ((JLabel) placePanel.get(0).getComponent()).setText(str);
        setPanel(placePanel);
    }

    public void placeCancelPanel(String str) {
        ((JLabel) cancelPlacePanel.get(0).getComponent()).setText(str);
        setPanel(cancelPlacePanel);
    }

    /**
     * @param n   Number of resources to be selected, -1 for any
     * @param p   the player inputting resources
     * @param str to display "on submit" button
     */
    public void inputResourcesPanel(final int n, final Player p, String str, final boolean YOP) {
        //final ArrayList<String> output = new ArrayList<String>();
        IRPDone = false;

        // Depopulates / Repopulates the combo boxes
        for (int i = 0; i < 5; i++) {
            ((JComboBox<Integer>) inputResourcesPanel.get(i).getComponent()).removeAllItems();
        }
        if (YOP) {
            for (int r = 0; r <= 2; r++) {
                ((JComboBox<Integer>) inputResourcesPanel.get(0).getComponent()).addItem((r));
            }
            for (int r = 0; r <= 2; r++) {
                ((JComboBox<Integer>) inputResourcesPanel.get(1).getComponent()).addItem((r));
            }
            for (int r = 0; r <= 2; r++) {
                ((JComboBox<Integer>) inputResourcesPanel.get(2).getComponent()).addItem((r));
            }
            for (int r = 0; r <= 2; r++) {
                ((JComboBox<Integer>) inputResourcesPanel.get(3).getComponent()).addItem((r));
            }
            for (int r = 0; r <= 2; r++) {
                ((JComboBox<Integer>) inputResourcesPanel.get(4).getComponent()).addItem((r));
            }
        } else {
            for (int r = 0; r <= p.getNumberResourcesType("BRICK"); r++) {
                ((JComboBox<Integer>) inputResourcesPanel.get(0).getComponent()).addItem((r));
            }
            for (int r = 0; r <= p.getNumberResourcesType("WOOL"); r++) {
                ((JComboBox<Integer>) inputResourcesPanel.get(1).getComponent()).addItem((r));
            }
            for (int r = 0; r <= p.getNumberResourcesType("ORE"); r++) {
                ((JComboBox<Integer>) inputResourcesPanel.get(2).getComponent()).addItem((r));
            }
            for (int r = 0; r <= p.getNumberResourcesType("GRAIN"); r++) {
                ((JComboBox<Integer>) inputResourcesPanel.get(3).getComponent()).addItem((r));
            }
            for (int r = 0; r <= p.getNumberResourcesType("LUMBER"); r++) {
                ((JComboBox<Integer>) inputResourcesPanel.get(4).getComponent()).addItem((r));
            }
        }

        //Set player
        ((JLabel) inputResourcesPanel.get(5).getComponent()).setText("Player: " + p.getName());

        // Sets action according to flag and resourceCount
        ((JButton) inputResourcesPanel.get(6).getComponent()).setAction(new AbstractAction() {
            public void actionPerformed(ActionEvent a) {
                int sum = 0;
                for (int i = 0; i < 5; i++) {
                    sum += ((JComboBox<Integer>) inputResourcesPanel.get(i).getComponent()).getSelectedIndex();
                }
                //System.out.println(sum);
                if (n != -1) {
                    if (sum != n) {
                        return;
                    }
                }
                ArrayList<String> output2 = new ArrayList<>();

                for (int i = 0; i < ((JComboBox<Integer>) inputResourcesPanel.get(0).getComponent()).getSelectedIndex(); i++) {
                    output2.add("BRICK");
                }
                for (int i = 0; i < ((JComboBox<Integer>) inputResourcesPanel.get(1).getComponent()).getSelectedIndex(); i++) {
                    output2.add("WOOL");
                }
                for (int i = 0; i < ((JComboBox<Integer>) inputResourcesPanel.get(2).getComponent()).getSelectedIndex(); i++) {
                    output2.add("ORE");
                }
                for (int i = 0; i < ((JComboBox<Integer>) inputResourcesPanel.get(3).getComponent()).getSelectedIndex(); i++) {
                    output2.add("GRAIN");
                }
                for (int i = 0; i < ((JComboBox<Integer>) inputResourcesPanel.get(4).getComponent()).getSelectedIndex(); i++) {
                    output2.add("LUMBER");
                }

                inputResources = output2;
                //System.out.println("Arrived");
                IRPDone = true;


            }
        });

        // Sets submit button text
        ((JButton) inputResourcesPanel.get(6).getComponent()).setText(str);

        setPanel(inputResourcesPanel);

    }

    public void setCurrentPlayer(Player p) {
        JLabel label = (JLabel) currentPlayerBox.getComponent();
        label.setText("Player: " + p.getName());
        label.setOpaque(true);
        label.setBackground(GameRunner.getCurrentPlayer().getColor());
    }

    public void setupPanel() {
        setPanel(setupPanel);
    }

    public void winPanel() {
        this.removeAll();

        JLabel win = new JLabel(GameRunner.getWinner().getName() + " wins!");
        win.setFont(new Font("Arial", Font.BOLD, 24));
        this.add(win, new Rectangle(2, 4, 10, 5));

        repaint();
        validate();
    }
}
