package sdn;

import entity.UE;

import java.util.ArrayList;

/**
 * 
 * @author Nathan Kong
 * @since Nov 2017
 */

// Assume static discovery and connection. Strictly means device specifically connected to this tower and that's it.
public class OvEnodeB {

    //This is now strictly a data plane object
    //Means that its forwarding should be adjusted by the controller
    //This should have a list of UEs that is maintains in its domain

    private ArrayList<UE> UEList;
    //private ArrayList<OvEnodeB> neighbors; Can implement this later if needed

    //Each of these objects should know the neighbors that it is connected to

    public OvEnodeB()
    {
        UEList = new ArrayList<>();
        //neighbors = new ArrayList<>();
    }

    public void addUE(UE e)
    {
        UEList.add(e);
    }


}
