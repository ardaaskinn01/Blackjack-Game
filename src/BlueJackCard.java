import java.util.Random;
import java.util.Scanner;

public class BlueJackCard {
    static int userScore = 0;
    static int compScore = 0;
    static int winner = 0;
    static boolean isPlayed1 = false;
    static boolean isPlayed2 = false;
    static boolean isPlayed3 = false;
    static boolean isPlayed4 = false;
    static int userChoices = 0;
    static int compChoices = 0;
    static int userPoint = 0;
    static int compPoint = 0;
    static int turn = 0;

    public static void main(String[] args) {
        System.out.println("-----------------------Bluejack Game!------------------------");
        startGame();
    }
    static class Card {
        String color;
        int num;

        Card(String color, int num) {
            this.color = color;
            this.num = num;
        }
    }


    public static int startGame(){
        if (winner == 1)
        {
            userScore++;
            System.out.println("Score: User " + userScore + "-" + compScore + " Computer");
            if (userScore == 3) {
                System.out.println("Game is over! User won the game.");
                return 0;
            }
        }
        else if (winner == 2)
        {
            compScore++;
            System.out.println("Score: Computer " + compScore + "-" + userScore + " User");
            if (compScore == 3) {
                System.out.println("Game is over! Comp won the game.");
                return 0;
            }
        }
        else {
            userScore = userScore;
            compScore = compScore;
        }
        Random rand = new Random();
        Card[] gameDeck = createGameDeck();
        Card[] userDeck = new Card[10];
        Card[] compDeck = new Card[10];
        // Dağıtılan kartların durumlarını saklamak için diziler
        boolean[] usedStatusUser = new boolean[gameDeck.length];
        boolean[] usedStatusComp = new boolean[gameDeck.length];
        gameDeck = shuffleCards(gameDeck);

        System.out.println("------------------------------------------------\n------------------------------------------------");
        System.out.println("Set Is Started!\n");

        // İlk 5 kartı dağıt
        for (int i = 0; i <= 4; i++) {
            compDeck[i] = gameDeck[i];
            userDeck[i] = gameDeck[gameDeck.length - 1 - i];

            // Dağıtılan kartların durumunu dizilere ekle
            usedStatusUser[i] = true;
            usedStatusComp[gameDeck.length - 1 - i] = true;

        }

        // Dağıtılan kartların durumunu işaretle
        // System.out.println("game deck1: " + listToString(gameDeck));
        deleteUsedCards(gameDeck, usedStatusUser, usedStatusComp);
        // System.out.println("game deck2: " + listToString(gameDeck));

        // Sonraki 3 kartı dağıt
        for (int b = 5; b < 8; b++) {
            // Rasgele kart seç ve dağıt
            int index = createRdSignedCard(gameDeck);
            int signChance = rand.nextInt(2); // 0 veya 1
            if (signChance == 0) {
                gameDeck[index].num *= -1;
            }
            compDeck[b] = gameDeck[index];
            usedStatusComp[index] = true;
            deleteUsedCards(gameDeck, usedStatusUser, usedStatusComp);
            // System.out.println("game deck3: " + listToString(gameDeck));

            int index2 = createRdSignedCard(gameDeck);
            int signChance2 = rand.nextInt(2); // 0 veya 1
            if (signChance2 == 0) {
                gameDeck[index2].num *= -1;
            }
            userDeck[b] = gameDeck[index2];
            usedStatusUser[index2] = true;
            deleteUsedCards(gameDeck, usedStatusUser, usedStatusComp);
            // System.out.println("game deck3: " + listToString(gameDeck));
        }

        // Dağıtılan kartların durumunu işaretle


        // 9. ve 10. kartları dağıt
        if (Math.random() < 0.2) {
            userDeck[8] = createFlip();
        } else {
            int index = lastTwoCard(gameDeck);
            userDeck[8] = gameDeck[index];
            usedStatusUser[index] = true;
            deleteUsedCards(gameDeck, usedStatusUser, usedStatusComp);
        }
        // System.out.println("game deck3: " + listToString(gameDeck));
        if (Math.random() < 0.2) {
            userDeck[9] = createDouble();
        } else {
            int index = lastTwoCard(gameDeck);
            userDeck[9] = gameDeck[index];
            usedStatusUser[index] = true;
            deleteUsedCards(gameDeck, usedStatusUser, usedStatusComp);
        }
        // System.out.println("game deck3: " + listToString(gameDeck));
        // 9. ve 10. kartları dağıt
        if (Math.random() < 0.2) {
            compDeck[8] = createFlip();
        } else {
            int index = lastTwoCard(gameDeck);
            compDeck[8] = gameDeck[index];
            usedStatusUser[index] = true;
            deleteUsedCards(gameDeck, usedStatusUser, usedStatusComp);
        }
        // System.out.println("game deck3: " + listToString(gameDeck));
        if (Math.random() < 0.2) {
            compDeck[9] = createDouble();
        } else {
            int index = lastTwoCard(gameDeck);
            compDeck[9] = gameDeck[index];
            usedStatusUser[index] = true;
            deleteUsedCards(gameDeck, usedStatusUser, usedStatusComp);
        }

        System.out.println("Computer Deck: " + listToString(compDeck));
        System.out.println("User Deck: " + listToString(userDeck));
        System.out.println("Remain Game Deck: " + listToString(gameDeck));
        System.out.println("Decks are ready, play!\n--------------------------------" );
        winner = game(gameDeck, userDeck, compDeck);
        startGame();
        return 0;
    }


    private static void deleteUsedCards(Card[] gameDeck, boolean[] usedStatusUser, boolean[] usedStatusComp) {
        for (int i = 0; i < gameDeck.length; i++) {
            if (usedStatusComp[i]) {
                gameDeck[i] = null; // Bilgisayar destesinden seçilen kartı null yap
            }
            if (usedStatusUser[i]) {
                gameDeck[i] = null; // Kullanıcı destesinden seçilen kartı null yap
            }
        }
    }


    private static Card[] createGameDeck(){
        String[] colors = {"Blue","Yellow", "Red", "Green" };
        int allCards = colors.length * 10; // 4 renk ve 10 değer olduğu için
        Card[] gameDeck = new Card[allCards]; //toplam kart sayısını hesaplamak için allcards
        int index =0;

        for (String color : colors ){ // nested loop her renk için değerleri sırayla alıp gameDecke ekler.
            for (int num =1; num <= 10; num++){
                gameDeck[index++] = new Card(color,num); // normal kartlar

            }
        }
        return gameDeck;// gameDeck kartların depolandığı dizi

    }
    private static void showGameDeck(Card[] gameDeck){
        for (Card card : gameDeck){
            System.out.println ("Color: " + card.color+ " num: " +card.num);
        }

    }


    public static Card[] shuffleCards(Card[] gameDeck){
        Random rd = new Random();
        for (int i =0; i<gameDeck.length; i++){

            int rdIndex = rd.nextInt(gameDeck.length);
            Card temp = gameDeck[i];
            gameDeck[i] = gameDeck[rdIndex];
            gameDeck[rdIndex]= temp;
        }

        return gameDeck;
    }

    private static int createRdSignedCard(Card[] gameDeck) {
        Random rand = new Random();
        int index;
        do {
            index = rand.nextInt(gameDeck.length);
        } while (gameDeck[index] == null || gameDeck[index].num > 6);
        return index;
    }

    private static int lastTwoCard(Card[] gameDeck) {
        Random rand = new Random();
        int index;
        do {
            index = rand.nextInt(gameDeck.length);
        } while (gameDeck[index] == null);
        return index;
    }
    private static Card createFlip(){
        return new Card ("Flip" ,0);
    }
    private static Card createDouble(){
        return new Card ("Double" ,0);
    }

    private static String listToString(Card[] list) {
        StringBuilder result = new StringBuilder("[");

        for (int i = 0; i < list.length; i++) {
            if (list[i] != null) {
                if (list[i].num != 0) {
                    result.append(list[i].color).append(" ");
                    if (list[i].num < 0) {
                        result.append("-").append(Math.abs(list[i].num));
                    } else {
                        result.append("+").append(list[i].num);
                    }
                } else {
                    result.append(list[i].color);
                }

                if (i < list.length - 1) {
                    result.append(", ");
                }
            }
        }
        result.append("]");
        return result.toString();
    }


    private static String indexToString(Card[] list, int startIndex, int endIndex) {
        StringBuilder result = new StringBuilder("[");

        for (int i = startIndex; i <= endIndex; i++) {
            if (list[i] != null) {
                if (list[i].num != 0) {
                    result.append(list[i].color).append(" ");
                    if (list[i].num < 0) {
                        result.append("-").append(Math.abs(list[i].num));
                    } else {
                        result.append("+").append(list[i].num);
                    }
                } else {
                    result.append(list[i].color);
                }

                if (i < endIndex) {
                    result.append(", ");
                }
            }
        }
        result.append("]");
        return result.toString();
    }

    private static int game(Card[] gameDeck, Card[] userDeck, Card[] compDeck){
        Random rand = new Random();
        Card[] userHand = new Card[4];
        Card[] compHand = new Card[4];
        Card[] blindHand = new Card[4];
        Card[] userBoard = new Card[9];
        Card[] compBoard = new Card[9];
        int userBlueCount = 0;
        int compBlueCount = 0;
        userPoint = 0;
        compPoint = 0;
        turn = 0;
        boolean[] usedStatusUser = new boolean[userDeck.length];
        boolean[] usedStatusComp = new boolean[compDeck.length];
        isPlayed1 = false;
        isPlayed2 = false;
        isPlayed3 = false;
        isPlayed4 = false;
        userChoices = 0;
        compChoices = 0;
        for(int i=0; i<4; i++)
        {
            int index;
            do {
                index = rand.nextInt(userDeck.length);
            } while (userDeck[index] == null);
            userHand[i] = userDeck[index];
            usedStatusUser[index] = true;
            deleteUsedCards(userDeck, usedStatusUser, usedStatusComp);
            if (userHand[i].color == "Blue") {
                userBlueCount++;
            }
            blindHand[i] = new Card("X", 0);
        }
        for(int i=0; i<4; i++)
        {
            int index;
            do {
                index = rand.nextInt(compDeck.length);
            } while (compDeck[index] == null);
            compHand[i] = compDeck[index];
            usedStatusComp[index] = true;
            deleteUsedCards(compDeck, usedStatusUser, usedStatusComp);
            if (compHand[i].color == "Blue") {
                compBlueCount++;
            }
        }
        System.out.println("Comp Hand:" + listToString(blindHand));
        System.out.println("Comp Board:" + listToString(compBoard));
        System.out.println("User Board:" + listToString(userBoard));
        System.out.println("User Hand:" + listToString(userHand) + "\n---------------------------------");
        if (userBlueCount == 4)
        {
            System.out.println("User won the set.");
            return 1;
        }
        if (compBlueCount == 4)
        {
            System.out.println("Computer won the set.");
            return 2;
        }

        while (userPoint != 20 && compPoint != 20){
            if (turn % 2 == 0){
                if (userChoices < 9 && userPoint <= 20) {
                    userTurn(userBoard, userHand, gameDeck, compBoard, blindHand);
                    turn++;
                }
                else if (userChoices == 9 && userPoint <= 20)
                {
                    int userDiff = 20 - userPoint;
                    int compDiff = 20 - compPoint;
                    if (userDiff < compDiff) {
                        System.out.println("User won the set.");
                        return 1;
                    }
                    else if (userDiff > compDiff) {
                        System.out.println("Computer won the set.");
                        return 2;
                    }
                    else {
                        System.out.println("Set is tied.");
                        return 3;
                    }
                }
                else if (userPoint>20){
                    System.out.println("User busts! Computer won the set.");
                    return 2;
                }
                else {
                    break;
                }
            }
            else {
                if (compPoint<20){
                    compTurn(compBoard, compHand, gameDeck, userBoard, userHand, blindHand);
                    turn++;
                }
                else if (compPoint>20){
                    System.out.println("Comp busts! User won the set.");
                    return 1;
                }
                else {
                    break;
                }
            }
        }
        if (userPoint == 20){
            System.out.println("User point is 20. User won the set!");
            return 1;
        }
        else if (compPoint == 20){
            System.out.println("Comp point is 20. Comp won the set!");
            return 2;
        }
        else {
            return 0;
        }
    }

    private static void userTurn(Card[] userBoard, Card[] userHand, Card[] gameDeck, Card[] compBoard, Card[] blindHand) {
        Random rand = new Random();
        Scanner scanner = new Scanner(System.in);
        boolean[] usedStatusUser = new boolean[gameDeck.length];
        boolean[] usedStatusComp = new boolean[gameDeck.length];
        if (isPlayed1) {
            userHand[0] = new Card("", 0);
        }
        if (isPlayed2) {
            userHand[1] = new Card("", 0);
        }
        if (isPlayed3) {
            userHand[2] = new Card("", 0);
        }
        if (isPlayed4) {
            userHand[3] = new Card("", 0);
        }
        int index;
        do {
            index = rand.nextInt(gameDeck.length);
        } while (gameDeck[index] == null);
        userBoard[userChoices] = gameDeck[index];
        usedStatusUser[index] = true;
        deleteUsedCards(gameDeck, usedStatusUser, usedStatusComp);
        userPoint += userBoard[userChoices].num;
        userChoices++;
        System.out.println("Comp Hand:" + listToString(blindHand));
        System.out.println("Comp Board:" + listToString(compBoard));
        System.out.println("User Board:" + listToString(userBoard));
        System.out.println("User Hand:" + listToString(userHand) + "\n---------------------------------");
        System.out.println("Play Cards or Stand!");
        System.out.println("0. Stand" + " 1. " + indexToString(userHand, 0, 0) + " 2. " + indexToString(userHand, 1, 1) + " 3. " + indexToString(userHand, 2, 2) + " 4. " + indexToString(userHand, 3, 3));
        int choose = scanner.nextInt();

        switch (choose) {
            case 0:
                System.out.println("You choose to stand.");
                break;
            case 1:
                if (isPlayed1 == false) {
                    System.out.println("You choose to play 1. card");
                    userBoard[userChoices] = userHand[0];
                    userChoices++;
                    isPlayed1 = true;
                    for (int i = userChoices - 1; i < userChoices; i++) {
                        if (userBoard[i].color == "Double") {
                            int doublePoints = userBoard[i-1].num;
                            userPoint += doublePoints;
                        }
                        else if (userBoard[userChoices - 1].color == "Flip") {
                            int reversePoints = userBoard[i-1].num * -1;
                            userPoint += reversePoints * 2;
                        }
                        else  {
                            userPoint += userBoard[i].num;
                        }
                    }
                    break;
                }
                else {
                    System.out.println("You played this card before. Please try again.");
                }
            case 2:
                if (isPlayed2 == false) {
                    System.out.println("You choose to play 2. card");
                    userBoard[userChoices] = userHand[1];
                    userChoices++;
                    isPlayed2 = true;
                    for (int i = userChoices - 1; i < userChoices; i++) {
                        if (userBoard[i].color == "Double") {
                            int doublePoints = userBoard[i-1].num;
                            userPoint += doublePoints;
                        }
                        else if (userBoard[userChoices - 1].color == "Flip") {
                            int reversePoints = userBoard[i-1].num * -1;
                            userPoint += reversePoints * 2;
                        }
                        else  {
                            userPoint += userBoard[i].num;
                        }
                    }
                    break;
                }
                else {
                    System.out.println("You played this card before. Please try again.");
                }
            case 3:
                if (isPlayed3 == false) {
                    System.out.println("You choose to play 3. card");
                    userBoard[userChoices] = userHand[2];
                    userChoices++;
                    isPlayed3 = true;
                    for (int i = userChoices - 1; i < userChoices; i++) {
                        if (userBoard[i].color == "Double") {
                            int doublePoints = userBoard[i-1].num;
                            userPoint += doublePoints;
                        }
                        else if (userBoard[userChoices - 1].color == "Flip") {
                            int reversePoints = userBoard[i-1].num * -1;
                            userPoint += reversePoints * 2;
                        }
                        else  {
                            userPoint += userBoard[i].num;
                        }
                    }
                    break;
                }
                else {
                    System.out.println("You played this card before. Please try again.");
                }
            case 4:
                if (isPlayed4 == false) {
                    System.out.println("You choose to play 4. card");
                    userBoard[userChoices] = userHand[3];
                    userChoices++;
                    isPlayed4 = true;
                    for (int i = userChoices - 1; i < userChoices; i++) {
                        if (userBoard[i].color == "Double") {
                            int doublePoints = userBoard[i-1].num;
                            userPoint += doublePoints;
                        }
                        else if (userBoard[userChoices - 1].color == "Flip") {
                            int reversePoints = userBoard[i-1].num * -1;
                            userPoint += reversePoints * 2;
                        }
                        else  {
                            userPoint += userBoard[i].num;
                        }
                    }
                    break;
                }
                else {
                    System.out.println("You played this card before. Please try again.");
                }

            default:
                System.out.println("Wrong choice. Please try again.");
        }

        System.out.println("Comp Hand:" + listToString(blindHand));
        System.out.println("Comp Board:" + listToString(compBoard));
        System.out.println("User Board:" + listToString(userBoard));
        System.out.println("User Hand:" + listToString(userHand) + "\n---------------------------------");
        // System.out.println(userPoint);
    }

    private static void compTurn(Card[] compBoard, Card[] compHand, Card[] gameDeck, Card[] userBoard, Card[] userHand, Card[] blindHand) {
        Random rand = new Random();
        boolean[] usedStatusUser = new boolean[gameDeck.length];
        boolean[] usedStatusComp = new boolean[gameDeck.length];
        int diffToTwenty = 20 - compPoint;
        int index;
        do {
            index = rand.nextInt(gameDeck.length);
        } while (gameDeck[index] == null);
        compBoard[compChoices] = gameDeck[index];
        usedStatusComp[index] = true;
        deleteUsedCards(gameDeck, usedStatusUser, usedStatusComp);
        compPoint += compBoard[compChoices].num;
        compChoices++;
        System.out.println("Comp Hand:" + listToString(blindHand));
        System.out.println("Comp Board:" + listToString(compBoard));
        System.out.println("User Board:" + listToString(userBoard));
        System.out.println("User Hand:" + listToString(userHand) + "\n---------------------------------");

        if (diffToTwenty == compHand[0].num) {
            compBoard[compChoices] = compHand[0];
            compPoint += compHand[0].num;
            blindHand[0] = new Card("O", 0);
            compChoices++;
        }
        else if (diffToTwenty == compHand[1].num) {
            compBoard[compChoices] = compHand[1];
            compPoint += compHand[1].num;
            blindHand[1] = new Card("O", 0);
            compChoices++;
        }
        else if (diffToTwenty == compHand[2].num) {
            compBoard[compChoices] = compHand[2];
            compPoint += compHand[2].num;
            blindHand[2] = new Card("O", 0);
            compChoices++;
        }
        else if (diffToTwenty == compHand[3].num) {
            compBoard[compChoices] = compHand[3];
            compPoint += compHand[3].num;
            blindHand[3] = new Card("O", 0);
            compChoices++;
        }

        if (compPoint > 20) {
            if (diffToTwenty > compHand[0].num) {
                compBoard[compChoices] = compHand[0];
                compPoint += compHand[0].num;
                blindHand[0] = new Card("O", 0);
                compChoices++;
            }
            else if (diffToTwenty > compHand[1].num) {
                compBoard[compChoices] = compHand[1];
                compPoint += compHand[1].num;
                blindHand[1] = new Card("O", 0);
                compChoices++;
            }
            else if (diffToTwenty > compHand[2].num) {
                compBoard[compChoices] = compHand[2];
                compPoint += compHand[2].num;
                blindHand[2] = new Card("O", 0);
                compChoices++;
            }
            else if (diffToTwenty > compHand[3].num) {
                compBoard[compChoices] = compHand[3];
                compPoint += compHand[3].num;
                blindHand[3] = new Card("O", 0);
                compChoices++;
            }
        }
    }
}