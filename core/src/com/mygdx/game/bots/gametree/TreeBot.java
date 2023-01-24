package com.mygdx.game.bots.gametree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import com.mygdx.game.bots.Bot;
import com.mygdx.game.bots.FitnessGroupBot;
import com.mygdx.game.bots.MaxN_Paranoid_Bot;
import com.mygdx.game.bots.OLABot;
import com.mygdx.game.bots.RandomBot;
import com.mygdx.game.coordsystem.Hexagon;
import com.mygdx.game.scoringsystem.ScoringEngine;

public class TreeBot extends Bot {

    private Hexagon.state playerstate;
    private Hexagon.state oppstate;

    private ScoringEngine SE;
    private Bot trainingbot;

    private int depthmax;
    private int depthlimit;

    private ArrayList<Root> rootsR;
    private ArrayList<Root> rootsB;

    private ArrayList<Root> optrootsR;
    private ArrayList<Root> optrootsB;

    
    /**
     * Tree Bot constructor
     * @param playerstate
     * @param oppstate
     */
    public TreeBot(Hexagon.state playerstate, Hexagon.state oppstate){
        this.playerstate = playerstate;
        this.oppstate = oppstate;

        rootsR = new ArrayList<Root>();
        rootsB = new ArrayList<Root>();
        optrootsR = new ArrayList<Root>();
        optrootsB = new ArrayList<Root>();

        SE = new ScoringEngine();

        //TWO VARIABLES BELOW CAN BE MODIFIED
        trainingbot = new MaxN_Paranoid_Bot(oppstate,playerstate);
        depthmax = 2;
    }

    /**
     * Method for setting the depth limit of the tree bot
     * @param field
     */
    public void setDepthLimit(ArrayList<Hexagon> field) {
        int tiles=0;
        for(Hexagon h:field) {
            if(h.getMyState()==Hexagon.state.BLANK) {
                tiles++;
            }
        }

        tiles = tiles - (field.size()%4);
        depthlimit = (tiles/2)-1;

        if(depthmax<depthlimit) {
            depthlimit=depthmax;
        }
    }

    /**
     * computes and executes the found moves on the board
     * @param field
     */
    public void calculate(ArrayList<Hexagon> field) {
        Random r = new Random();
        int rnum;

        rootsR = new ArrayList<Root>();
        rootsB = new ArrayList<Root>();
        optrootsR = new ArrayList<Root>();
        optrootsB = new ArrayList<Root>();

        setDepthLimit(field);

        computeRoots(field, Hexagon.state.RED, playerstate);
        setRoots(playerstate);

        rnum = r.nextInt(optrootsR.size());
        for(Hexagon hex:field) {
            if(optrootsR.get(rnum).getRootQ()==hex.getQ()&&optrootsR.get(rnum).getRootR()==hex.getR()) {

                hex.setMyState(Hexagon.state.RED);
            }
        }


        computeRoots(field, Hexagon.state.BLUE, playerstate);
        setRoots(playerstate);

        rnum = r.nextInt(optrootsB.size());
        for(Hexagon hex:field) {
            if(optrootsB.get(rnum).getRootQ()==hex.getQ()&&optrootsB.get(rnum).getRootR()==hex.getR()) {
                hex.setMyState(Hexagon.state.BLUE);
            }
        }

    }

    /**
     * Debug tool which prints the optimal roots
     * @param player
     */
    public void printOptimalRoots(Hexagon.state player) {
        System.out.println("For playing as the colour "+player);
        for(Root r:optrootsR) {
            System.out.println("Optimal Red places are: q "+r.getRootQ()+". r "+r.getRootR()+ ". WITH SCORE: "+r.getScore());
        }
        for(Root r:optrootsB) {
            System.out.println("Optimal Blue places are: q "+r.getRootQ()+". r "+r.getRootR()+ ". WITH SCORE: "+r.getScore());
        }
    }

    /**
     * Set and choose the best root
     * @param player
     */
    public void setRoots(Hexagon.state player) {
        int best = Integer.MIN_VALUE;
        int worst = Integer.MIN_VALUE;


        for(Root r:rootsR) {
                //System.out.println(player + " PLAYER STATE PLACING RED ROOTS, no. of score "+r.getScore2());
            if(player==Hexagon.state.RED) {
                if (r.getScore2()>best) {
                    best=r.getScore2();
                }
            } else if(player==Hexagon.state.BLUE) {
                if(r.getScore2()>worst) {
                    worst=r.getScore2();
                }
            }
        }

        for(Root r:rootsR) {
            if(player==Hexagon.state.RED) {
                if(r.getScore2()==best) {
                    optrootsR.add(r);
                }
            } else if(player==Hexagon.state.BLUE) {
                if(r.getScore2()==worst) {
                    optrootsR.add(r);
                }
            }
        }

        //--

        for(Root r:rootsB) {
            //System.out.println(player + " PLAYER STATE PLACING BLUE ROOTS, no. of score "+r.getScore2());
            if(player==Hexagon.state.BLUE) {
                if (r.getScore2()>best) {
                    best=r.getScore2();
                }
            } else if(player==Hexagon.state.RED) {
                if(r.getScore2()>worst) {
                    worst=r.getScore2();
                }
            }
        }

        for(Root r:rootsB) {
            if(player==Hexagon.state.BLUE) {
                if(r.getScore2()==best) {
                    optrootsB.add(r);
                }
            } else if(player==Hexagon.state.RED) {
                if(r.getScore2()==worst) {
                    optrootsB.add(r);
                }
            }
        }
    }

    /**
     * Compute the all the scores of the roots by recursively going down the tree untill the depth limit is met
     * @param field
     * @param color
     * @param player
     */

    public void computeRoots(ArrayList<Hexagon> field, Hexagon.state color, Hexagon.state player) {
        // Copy the current state of the board
        ArrayList<Hexagon> clone = new ArrayList<Hexagon>();
        try {
            for(Hexagon h : field) {
                clone.add(h.clone());
            }
        } catch (Exception e) {}


        for(Hexagon hc : clone) {
            if(hc.getMyState()==Hexagon.state.BLANK) {
                hc.setMyState(color);
                Root root = new Root(hc.getQ(),hc.getR(),hc.getMyState(),player);

                if(color==Hexagon.state.RED) {
                    rootsR.add(root);
                } else if(color==Hexagon.state.BLUE) {
                    rootsB.add(root);
                }

                if(depthlimit==0) {

                    SE.calculate(clone);
                    root.addLeaf2(SE.getRedScore()-SE.getBlueScore());

                    // if(root.getRootState()==Hexagon.state.RED) {
                    //     root.addLeaf2(SE.getRedScore()-SE.getBlueScore());
                    // } else if(root.getRootState()==Hexagon.state.BLUE) {
                    //     root.addLeaf2(SE.getBlueScore()-SE.getRedScore());
                    // }
                }

                ArrayList<Hexagon> clone2 = new ArrayList<Hexagon>();
                try {
                    for(Hexagon h : clone) {
                        clone2.add(h.clone());
                    }
                } catch (Exception e) {}

                if(color==Hexagon.state.BLUE && depthlimit>0) {
                    recursion(clone2, 1, Hexagon.state.RED, player, root);
                } else if(color==Hexagon.state.RED && depthlimit>0) {
                    recursion(clone2, 1, Hexagon.state.BLUE, player, root);
                }

                hc.setMyState(Hexagon.state.BLANK);
            }
        }

    }

    /**
     * The recursion method which is used inside compute roots 
     * @param recursionfield
     * @param d
     * @param color
     * @param player
     * @param root
     */
    public void recursion(ArrayList<Hexagon> recursionfield, int d, Hexagon.state color, Hexagon.state player, Root root) {
        if(d>depthlimit) return;

        if(color==Hexagon.state.RED) {
            trainingbot.calculate(recursionfield);
        }
        
        for(Hexagon hcc : recursionfield) {
            if(hcc.getMyState()==Hexagon.state.BLANK){
                hcc.setMyState(color);

                if(d==depthlimit) {
                    SE.calculate(recursionfield);
                    //System.out.println(SE.getRedScore()+ "   " +SE.getBlueScore() + " FOR ROOT Q: "+ root.getRootQ()+ "  R: "+root.getRootR());
                    root.addLeaf2(SE.getRedScore()-SE.getBlueScore());
                }

                ArrayList<Hexagon> clone3 = new ArrayList<Hexagon>();
                try {
                    for(Hexagon h : recursionfield) {
                        clone3.add(h.clone());
                    }
                } catch (Exception e) {}

                if(color==Hexagon.state.BLUE) {
                    recursion(clone3, d+1, Hexagon.state.RED, player, root);
                } else if(color==Hexagon.state.RED) {
                    recursion(clone3, d+1, Hexagon.state.BLUE, player, root);
                }

                hcc.setMyState(Hexagon.state.BLANK);
            }
        }

    }


}
