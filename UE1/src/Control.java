import java.util.Scanner;

/** Main of UE1. Execute & do via Terminal **/
public class Control {
    public static void main(String[] args){
        Scanner s = new Scanner(System.in);
        int choice = 0;

        Ue1_1 e1 = new Ue1_1();
        Ue1_2 e2 = new Ue1_2();
        Ue1_3 e3 = new Ue1_3();
        Ue1_4 e4 = new Ue1_4();

        do {
            System.out.println("####################\n" +
                    "### Mainprogramm ###\nWhich exersice to execute?\n" +
                    "Options: 1, 2, 3, 4. Otherwise, enter 5 in order to exit.");
            choice = s.nextInt();
            switch (choice) {
                case 1:
                    e1.init();
                    break;
                case 2:
                    e2.init();
                    break;
                case 3:
                    e3.init();
                    break;
                case 4:
                    e4.init();
                    break;
                case 5:
                    System.exit(0);
                default:
                    System.out.println("Unable to determine exersice. Try again.");
                    break;
            }
        }while(true);
    }
}
