/*
Jack Krejci
Grocery Store 
 */

import java.util.Scanner;
import javax.script.CompiledScript;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;



//MAIN CLASS
class BillProcessor {
    public static void prepareBill(LinkedHashMap<String, Integer> purchases, LinkedHashMap<String, Double> itemsAll,
                                LinkedHashMap<String, String> sales, ArrayList<String> itemNames) {
        double regCost, singleRegCost, totalCost = 0, discount = 0, totalBill = 0, savedMoney = 0;
        int numBogos;
        String theSale;
        int totalPurchases;
        
        //For loop to calculate all purchase information
        System.out.println("\nHere is what you purchased:");
        for (String itemPurchased : purchases.keySet()) {
            //For all the items purchased included in purchases LHM
            regCost = purchases.get(itemPurchased) * itemsAll.get(itemPurchased);
            
            //Adjust the regular cost if there's a sale
            if (sales.containsKey(itemPurchased)) {
                //if it's buy one get one free
                if (sales.get(itemPurchased).equals("bogo")) {
                    //Number of bogo's = the total number of purchases of that item integer divided by 2 (if 5 then 2 bogos for example)
                    numBogos = purchases.get(itemPurchased) / 2;
                    
                    //Adjust the regular cost to total from how many bogos there are only needed to pay for the total number of those items minus bogos
                    discount = numBogos * itemsAll.get(itemPurchased);
                    totalCost = (purchases.get(itemPurchased) * itemsAll.get(itemPurchased)) - discount;
                    savedMoney += discount;
                } else { //If the discount is not buy one get one
                    //Get what the sale is on that item and that's its total cost
                    theSale = sales.get(itemPurchased);
                    discount = purchases.get(itemPurchased) * Double.parseDouble(theSale);
                    totalCost = (purchases.get(itemPurchased) * itemsAll.get(itemPurchased)) - discount;
                    savedMoney += discount;
                }
                     
            } else {
                //get the total cost if there's not a sale
                totalCost = regCost;
            }

            //Add to the bill and also note detailed information about the purchase
            totalBill += totalCost;
            totalPurchases = purchases.get(itemPurchased);
            singleRegCost = itemsAll.get(itemPurchased);

            //Summary if there's a discount
            if (totalCost != regCost) {
                System.out.printf("\n%d %s, regularly $%.2f each, total $%.2f ($%.2f discount)", totalPurchases, itemPurchased, singleRegCost, totalCost, discount);
            } else {
                //Summary if there's no discount
                System.out.printf("\n%d %s, regularly $%.2f each, total $%.2f", totalPurchases, itemPurchased, singleRegCost, totalCost);

            }
            


        }

        //Print the summary of the buyers purchases
        System.out.printf("\n\nYour total bill is $%.2f.", totalBill);
        System.out.printf("\nYou saved $%.2f by shopping with us today\n", savedMoney);
            }
}



//Class which contains main code 
public class KrejciGroceryStore {
    //Bring in static functions here
    private static LinkedHashMap<String, Double> itemsAll; //Stores items/cost in this hashmap including bogo
    private static LinkedHashMap<String, String> sales; //Stores items and sale value
    private static LinkedHashMap<String, Integer> purchases; //Storage for items purchsed 
    private static ArrayList<String> itemNames; //Array to list out items in the menu
    

    /**
     * Function for linked hashmap that reads items from file 
     * @param fname - file name
     * @return
     */
    public static LinkedHashMap<String, Double> readItemsAllFromFile(String fname) {
        LinkedHashMap<String, Double> result = new LinkedHashMap<String, Double>();
        String line, itemName;
        double itemPrice;
        String[] parts;
        try {
            Scanner fsc = new Scanner(new File(fname));
            while (fsc.hasNextLine()) {
                line = fsc.nextLine().trim();
                //Split the line from it's tabs in the file
                if (line.length() > 0) {
                    parts = line.split("\t");
                    itemName = parts[0];
                    itemPrice = Double.parseDouble(parts[1]);
                    //Add the item to the LHM result
                    result.put(itemName, itemPrice);
                }

            }
            fsc.close();
        } catch(Exception ex) {
            System.out.println("Error occured For first.");
        }

        return result;
    }


    /**
     * Function for the item names only to be used 
     * @param fname - file name
     * @return
     */
    public static ArrayList<String> readItemNamesFromFile(String fname) {
        ArrayList<String> itemNames = new ArrayList<String>();
        String line, itemName;
        String[] parts;
        try {
            Scanner fsc = new Scanner(new File(fname));
            while (fsc.hasNextLine()) {
                line = fsc.nextLine().trim();
                //Split the line from it's tabs in the file
                if (line.length() > 0) {
                    parts = line.split("\t");
                    itemName = parts[0];
                    //Add itemNames to itemName array
                    itemNames.add(itemName);
                }

            }
            fsc.close();
        } catch(Exception ex) {
            System.out.println("Error occured For third.");
        }
        Collections.sort(itemNames);
        return itemNames;
    }

    /**
     * Function for linked hash map sale readings
     * @param fname - file name
     * @return
     */
    public static LinkedHashMap<String, String> readSalesFromFile(String fname) {
        LinkedHashMap<String, String> result = new LinkedHashMap<String, String>();
        //reads contents from sales txt file use a string so you can use bogo as well
        String saleItem, saleValue, line;
        String[] parts;
        try {
            Scanner fsc = new Scanner(new File(fname));
            while (fsc.hasNextLine()) {
                line = fsc.nextLine().trim();
                //Split the line from it's tabs in the file
                if (line.length() > 0) {
                    parts = line.split("\t");
                    saleItem = parts[0];
                    saleValue = parts[1];
                    //Add the item names to itemNames array and item and its price to the LHM result
                    result.put(saleItem, saleValue);
                    }

            }
            fsc.close();
        } catch(Exception ex) {
            System.out.println("Error occured. For Second");
        }
        
        return result;
    }


    /**
     * Function for taking the user choice and adding item to their list with price 
     * @param userChoice - user choice input
     * @param purchases - purchases LinkedHashMap for amount of purchases of an item
     * @param itemNames - ArrayList for the names of the items
     * @return
     */
    public static LinkedHashMap<String, Integer> getPurchasesFromUser(String userChoice, LinkedHashMap<String, Integer> purchases, 
                                                                        ArrayList<String> itemNames) {
        //Get the correct item according to the index of the itemNames                                                         
        int theChoice = Integer.parseInt(userChoice) - 1;
        String itemSelect;

        itemSelect = itemNames.get(theChoice);

        //If the item is already in the purchases list add one to the amount of items it has for that item
        if (purchases.containsKey(itemSelect)) {
            int numItems = purchases.get(itemSelect);
            purchases.remove(itemSelect);
            purchases.put(itemSelect, numItems + 1);

        //If not put that new item in the array and set the amount of items we have of it equal to 1
        } else {
            purchases.put(itemSelect, 1);
        }

        return purchases;
    }


    /**
     * Function for presenting the menu to the user 
     * @param itemNames - ArrayList keeping track of item names
     * @param itemsAll - LinkedHashMap keeping track of the price of items
     * @param sales - LinkedHashMap keeping track of possible sales
     */
    public static void presentMenuOfItems(ArrayList <String> itemNames, LinkedHashMap<String, Double> itemsAll, LinkedHashMap<String, String> sales) {
        String num = "##";
        String strItem = "Item Name";
        String strSale = "Reg. Sale";

        System.out.printf("%-5s %s %20s\n", num, strItem, strSale);
        System.out.println("---------------------------------------------------------"); //57 dashes


        for(String itemName : itemNames) {
            //Need to get the price and sale for that item name if any
            double itemPrice = itemsAll.get(itemName);
            String itemSale = sales.get(itemName);
            int itemNumber = itemNames.indexOf(itemName) + 1;
            if (itemSale == null) {
                System.out.printf("%-5d %-20s %.2f\n", itemNumber, itemName, itemPrice); //Print item number, name, price without sale 
            } else if (itemSale.equals("bogo")) {
                System.out.printf("%-5d %-20s %.2f Buy One, Get One\n", itemNumber, itemName, itemPrice, itemSale); //Print item number, name, price/sale
            } else {
                System.out.printf("%-5d %-20s %.2f $%2s discount\n", itemNumber, itemName, itemPrice, itemSale); //Print item number, name, price/sale

            }

        }
        System.out.println("---------------------------------------------------------\n"); //57 dashes

    }


    //Main  Code (brings the user to make a decision on given items or quit) 
    //and calls the class to show them the results
    public static void main(String[] args) {
        //Declare or declare and initialize variables
        //Get the hash maps
        //REMEMBER TO CHANGE THE PATHS TO SALES.TXT AND ITEMS.TXT BEFORE YOU TURN IN JUST USE THE PATHS TO CHECK 
        itemsAll = readItemsAllFromFile("items.txt");
        sales = readSalesFromFile("sales.txt");
        LinkedHashMap<String, Integer> purchases = new LinkedHashMap<String, Integer>(); 
        itemNames = readItemNamesFromFile("items.txt");; //Get the items only from the items.txt file
        Scanner sc = new Scanner(System.in); 
        String userChoice; //Asks the user if they still want to make a choice
        int numberUserChoice;

        //Present the title of the program to user
        System.out.println("***************************************************************");
        System.out.println("*                      CHARLIE'S PANTRY                       *");
        System.out.println("***************************************************************");

        //Present the welcome message to the user 
        System.out.println("\nWelcome to your friendly neighborhood Charlie's Pantry. We sell");
        System.out.println("only the highest quality groceries and freshest produce around.");
        System.out.println("We have many great specials this week. The more you buy, the");
        System.out.println("more you save!\n");

        //Ask the user what they'd like to buy and present the table of items to them
        System.out.println("What would you like to buy?\n");
        
        //Present the user with the menu with all the different choices of items and prices
        presentMenuOfItems(itemNames, itemsAll, sales);

        //Present with a choice and take everything from there
        do {
            //ask them for choice (use sc)
            System.out.print("Enter the number of your choice, or q to check out: ");
            userChoice = sc.nextLine();

            if (!(userChoice.equalsIgnoreCase("q"))) {
                numberUserChoice = Integer.parseInt(userChoice);

                if (numberUserChoice > 0 && numberUserChoice < 16) {
                    //Call the purchases function to calculate what and how many items chosen to buy
                    getPurchasesFromUser(userChoice, purchases, itemNames);
                } else {
                    System.out.println("\nInvalid Choice! Please try again.");
                }
            }

        } while (!(userChoice.equalsIgnoreCase("q")));

    //Call the bill processor class to calculate costs, savings, and to show the receipt to the user
    BillProcessor.prepareBill(purchases, itemsAll, sales, itemNames);
    
    //Thank user for using the program
    System.out.println("\nThank you for your business. Come back soon!");
    }

}