package app;

import gameSpaces.*;

public class Board {

    public boardSpace[] boardSpaces;
    public boardSpace GO;
    public boardSpace JAIL;
    public Property BOARDWALK;
    public Property STCHARLESPLACE;
    public Property ILLINOISAVE;
    public Property READINGRAILROAD;

    public Board()
    {
        boardSpaces = new boardSpace[40];
        initBoard();
    }

    private void initBoard()
    {
        //Create boardSpaces
        Property mediterraneanAvenue = new brownProperty(60, new int[]{2, 10, 30, 90, 160, 250}, "Mediterranean Avenue", 1);
        Property balticAvenue = new brownProperty(60, new int[]{4, 20, 60, 180, 320, 450}, "Baltic Avenue", 3);
        mediterraneanAvenue.addPropertyToSet(balticAvenue);
        balticAvenue.addPropertyToSet(mediterraneanAvenue);

        Property orientalAvenue = new lightBlueProperty(100, new int[]{6, 30, 90, 270, 400, 550}, "Oriental Avenue", 6);
        Property vermontAvenue = new lightBlueProperty(100, new int[]{6, 30, 90, 270, 400, 550}, "Vermont Avenue", 8);
        Property connecticutAvenue = new lightBlueProperty(120, new int[]{8, 40, 100, 300, 450, 600}, "Connecticut Avenue", 9);
        orientalAvenue.addPropertyToSet(vermontAvenue, connecticutAvenue);
        vermontAvenue.addPropertyToSet(orientalAvenue, connecticutAvenue);
        connecticutAvenue.addPropertyToSet(orientalAvenue, vermontAvenue);

        Property stCharlesPlace = new pinkProperty(140, new int[]{10, 50, 150, 450, 625, 750}, "St. Charles Place", 11);
        Property statesAvenue = new pinkProperty(140, new int[]{10, 50, 150, 450, 625, 750}, "States Avenue", 13);
        Property virginiaAvenue = new pinkProperty(160, new int[]{12, 60, 180, 500, 700, 900}, "Virginia Avenue", 14);
        stCharlesPlace.addPropertyToSet(statesAvenue, virginiaAvenue);
        statesAvenue.addPropertyToSet(stCharlesPlace, virginiaAvenue);
        virginiaAvenue.addPropertyToSet(stCharlesPlace, statesAvenue);
        this.STCHARLESPLACE = stCharlesPlace;

        Property stJamesPlace = new orangeProperty(180, new int[]{14, 70, 200, 550, 750, 950}, "St. James Place", 16);
        Property tennesseeAvenue = new orangeProperty(180, new int[]{14, 70, 200, 550, 750, 950}, "Tennessee Avenue", 18);
        Property newJerseyAvenue = new orangeProperty(200, new int[]{16, 80, 220, 600, 800, 1000}, "New Jersey Avenue", 19);
        stJamesPlace.addPropertyToSet(tennesseeAvenue, newJerseyAvenue);
        tennesseeAvenue.addPropertyToSet(stJamesPlace, newJerseyAvenue);
        newJerseyAvenue.addPropertyToSet(stJamesPlace, tennesseeAvenue);

        Property kentuckyAvenue = new redProperty(220, new int[]{18, 90, 250, 700, 875, 1050}, "Kentucky Avenue", 21);
        Property indianaAvenue = new redProperty(220, new int[]{18, 90, 250, 700, 875, 1050}, "Indiana Avenue", 23);
        Property illinoisAvenue = new redProperty(240, new int[]{20, 100, 300, 750, 925, 1100}, "Illinois Avenue", 24);
        kentuckyAvenue.addPropertyToSet(indianaAvenue, illinoisAvenue);
        indianaAvenue.addPropertyToSet(kentuckyAvenue, illinoisAvenue);
        illinoisAvenue.addPropertyToSet(kentuckyAvenue, indianaAvenue);
        this.ILLINOISAVE = illinoisAvenue;

        Property atlanticAvenue = new yellowProperty(260, new int[]{22, 110, 330, 800, 975, 1150}, "Atlantic Avenue", 26);
        Property ventnorAvenue = new yellowProperty(260, new int[]{22, 110, 330, 800, 975, 1150}, "Ventnor Avenue", 27);
        Property marvinGardens = new yellowProperty(280, new int[]{24, 120, 360, 850, 1025, 1200}, "Marvin Gardens", 29);
        atlanticAvenue.addPropertyToSet(ventnorAvenue, marvinGardens);
        ventnorAvenue.addPropertyToSet(atlanticAvenue, marvinGardens);
        marvinGardens.addPropertyToSet(atlanticAvenue, ventnorAvenue);

        Property pacificAvenue = new greenProperty(300, new int[]{26, 130, 390, 900, 1100, 1275}, "Pacific Avenue", 31);
        Property northCarolinaAvenue = new greenProperty(300, new int[]{26, 130, 390, 900, 1100, 1275}, "North Carolina Avenue", 32);
        Property pennsylvaniaAvenue = new greenProperty(320, new int[]{28, 150, 450, 1000, 1200, 1400}, "Pennsylvania Avenue", 34);
        pacificAvenue.addPropertyToSet(northCarolinaAvenue, pennsylvaniaAvenue);
        northCarolinaAvenue.addPropertyToSet(pacificAvenue, pennsylvaniaAvenue);
        pennsylvaniaAvenue.addPropertyToSet(pacificAvenue, northCarolinaAvenue);

        Property parkPlace = new darkBlueProperty(350, new int[]{35, 175, 500, 1100, 1300, 1500}, "Park Place", 37);
        Property boardwalk = new darkBlueProperty(400, new int[]{50, 200, 600, 1400, 1700, 2000}, "Boardwalk", 39);
        parkPlace.addPropertyToSet(boardwalk);
        boardwalk.addPropertyToSet(parkPlace);
        this.BOARDWALK = boardwalk;

        Property readingRailroad = new railroadProperty(200, new int[]{25}, "Reading Railroad", 5);
        Property pennsylvaniaRailroad = new railroadProperty(200, new int[]{25}, "Pennsylvania Railroad", 15);
        Property boRailroad = new railroadProperty(200, new int[]{25}, "B & O Railroad", 25);
        Property shortLine = new railroadProperty(200, new int[]{25}, "Short Line", 35);
        readingRailroad.addPropertyToSet(pennsylvaniaRailroad, boRailroad, shortLine);
        pennsylvaniaRailroad.addPropertyToSet(readingRailroad, boRailroad, shortLine);
        boRailroad.addPropertyToSet(readingRailroad, pennsylvaniaRailroad, shortLine);
        shortLine.addPropertyToSet(readingRailroad, pennsylvaniaRailroad, boRailroad);
        this.READINGRAILROAD = readingRailroad;

        Property electricCompany = new utilityProperty(150, "Electric Company", 4, 10, 12);
        Property waterWorks = new utilityProperty(150, "Water Works", 4, 10, 28);
        electricCompany.addPropertyToSet(waterWorks);
        waterWorks.addPropertyToSet(electricCompany);

        this.GO = new goSpace(200, 0, "Go!");
        this.JAIL = new jailSpace(10, "Jail");
        boardSpace freeParking = new freeParkingSpace(20, "Free Parking");
        boardSpace goToJail = new goToJailSpace(this.JAIL.spacePosition, 30, "Go To Jail");

        boardSpace community1 = new chanceCommunitySpace(2, "Community Chest", chanceCommunitySpace.deckType.COMMUNITYCHEST);
        boardSpace community2 = new chanceCommunitySpace(17, "Community Chest", chanceCommunitySpace.deckType.COMMUNITYCHEST);
        boardSpace community3 = new chanceCommunitySpace(33, "Community Chest", chanceCommunitySpace.deckType.COMMUNITYCHEST);

        boardSpace chance1 = new chanceCommunitySpace(7, "Chance", chanceCommunitySpace.deckType.CHANCE);
        boardSpace chance2 = new chanceCommunitySpace(22, "Chance", chanceCommunitySpace.deckType.CHANCE);
        boardSpace chance3 = new chanceCommunitySpace(36, "Chance", chanceCommunitySpace.deckType.CHANCE);

        boardSpace incomeTax = new taxSpace(200, 4, "Income Tax");
        boardSpace luxuryTax = new taxSpace(75, 38, "Luxury Tax");

        //Add boardSpaces to boardSpaces array
        boardSpaces[0] = this.GO;
        boardSpaces[1] = mediterraneanAvenue;
        boardSpaces[2] = community1;
        boardSpaces[3] = balticAvenue;
        boardSpaces[4] = incomeTax;
        boardSpaces[5] = readingRailroad;
        boardSpaces[6] = orientalAvenue;
        boardSpaces[7] = chance1;
        boardSpaces[8] = vermontAvenue;
        boardSpaces[9] = connecticutAvenue;
        boardSpaces[10] = this.JAIL;
        boardSpaces[11] = stCharlesPlace;
        boardSpaces[12] = electricCompany;
        boardSpaces[13] = statesAvenue;
        boardSpaces[14] = virginiaAvenue;
        boardSpaces[15] = pennsylvaniaRailroad;
        boardSpaces[16] = stJamesPlace;
        boardSpaces[17] = community2;
        boardSpaces[18] = tennesseeAvenue;
        boardSpaces[19] = newJerseyAvenue;
        boardSpaces[20] = freeParking;
        boardSpaces[21] = kentuckyAvenue;
        boardSpaces[22] = chance2;
        boardSpaces[23] = indianaAvenue;
        boardSpaces[24] = illinoisAvenue;
        boardSpaces[25] = boRailroad;
        boardSpaces[26] = atlanticAvenue;
        boardSpaces[27] = ventnorAvenue;
        boardSpaces[28] = waterWorks;
        boardSpaces[29] = marvinGardens;
        boardSpaces[30] = goToJail;
        boardSpaces[31] = pacificAvenue;
        boardSpaces[32] = northCarolinaAvenue;
        boardSpaces[33] = community3;
        boardSpaces[34] = pennsylvaniaAvenue;
        boardSpaces[35] = shortLine;
        boardSpaces[36] = chance3;
        boardSpaces[37] = parkPlace;
        boardSpaces[38] = luxuryTax;
        boardSpaces[39] = boardwalk;

    }

}