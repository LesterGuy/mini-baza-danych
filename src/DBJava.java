// Projekt zostal napisany przez:
// Jaroslaw Maliszewski
// 20201

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import static java.lang.System.exit;

public class DBJava {
    public static void main(String[] argv) throws IOException {
        while(true) program.menu();
    }
}
class Dane implements Serializable {
    private static final long serialVersionUID = 3190874088669404340l;
    int id;
    String nadawca;
    String odbiorca;
    Date data;
    String tresc;

    public Dane() {
    }

    int getId(){
        return this.id;
    }

    String getNadawca(){
        return this.nadawca;
    }

    String getOdbiorca(){
        return this.odbiorca;
    }

    Date getData(){
        return this.data;
    }

    String getTresc(){
        return this.tresc;
    }
}
class program{
    static String otwartyPlik = "brak";
    static ArrayList<Dane>list = new ArrayList<>();
    public static void menu() throws IOException {
        char ch;

        System.out.println("Aktualnie otwarta baza danych: "+otwartyPlik);
        System.out.println(" ============================");
        System.out.println("\n 1.Otworz baze danych");
        System.out.println("\n 2.Utworz nowa baze");
        System.out.println("\n 3.Przeglad bazy");
        System.out.println("\n 4.Sortowanie bazy");
        System.out.println("\n 5 Usun baze");
        System.out.println("\n 6.Zakoncz program");
        System.out.println("\n ===========================");
        System.out.println("\n Wybierz opcje : ");

        do {
            ch = (char) System.in.read();
        } while (ch != '1' && ch != '2' && ch != '3' && ch != '4' && ch != '5' && ch != '6' && ch != 27);

        switch(ch){
            case '1':
                System.out.println("\n Wybrano opcje 1\n");
                otworzBaze();
                break;
            case '2':
                System.out.println("\n Wybrano opcje 2\n");
                utworzBaze();
                break;
            case '3':
                System.out.println("\n Wybrano opcje 3\n");
                przegladBazy();
                break;
            case '4':
                System.out.println("\n Wybrano opcje 4\n");
                sortowanieBazy();
                break;
            case '5':
                System.out.println("\n Wybrano opcje 5\n");
                usunBaze();
                break;
            case '6':
                System.out.println("\n Wychodzenie z programu\n");
                zapisz();
                otwartyPlik = "brak";
                exit(0);
                break;
            }
    }

    public static void otworzBaze() throws IOException {
        String nazwa;
        Scanner sc = new Scanner(System.in);
        boolean bool;

        cls();

        do {
            bool = false;
            System.out.print("Podaj nazwe bazy, wybierz 6 aby wyjsc: ");
            nazwa = sc.next();
            if(nazwa.equals("6")) {
                cls();
                return;
            }
            if(otwartyPlik.equals(nazwa)) {
                System.out.println("Ten plik jest juz otwarty\n");
                bool = true;
            }
            else {
                bool = !sprawdzIstnienie(nazwa);
            }
        }while(bool);

        otwartyPlik = nazwa;

        list.clear();

        try{
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(nazwa));
            list = (ArrayList<Dane>) in.readObject();
            in.close();
        }catch(ClassNotFoundException exc){
            System.out.println(exc);
        }catch(FileNotFoundException e){
            System.out.println("Plik nie zostal odnaleziony.");
        }catch(EOFException ex) {
            System.out.println();
        }
        cls();
        return;
    }

    public static void utworzBaze() {
        boolean bool;
        String nazwa;
        Scanner sc = new Scanner(System.in);
        File f;

        cls();

        do{
            bool = false;
            System.out.println("Podaj nazwe bazy, wybierz 6 aby wyjsc: ");
            nazwa = sc.next();
            f = new File(nazwa);

            if(nazwa.equals("6")) {
                System.out.println("cls");
                return;
            }
            if(f.exists() && !f.isDirectory()){
                System.out.println("Plik zostal juz utworzony");
                bool = true;
            }
            if(otwartyPlik.equals(nazwa)){
                System.out.println("Ten plik jest otwarty w tym momencie\n");
                bool = true;
            }
            if(!sprawdzIstnienie(nazwa)){
                bool = true;
            }
        }while(bool);

        File newFile = new File(nazwa);
        if (!newFile.exists()) {
            try {
                newFile.createNewFile();
                System.out.println("Nowy plik " + nazwa + " został utworzony");
            } catch (IOException e) {
                System.out.println("Plik " + nazwa + " nie został utworzony");
            }
        }

        cls();
        System.out.println("Plik został utworzony");
        return;
    }

    public static void usunBaze() throws IOException {
        Scanner sc = new Scanner(System.in);
        int p;
        char n;
        String nazwa;
        do {
            p = 0;
            System.out.println("Podaj nazwe bazy, wybierz 6 aby wyjsc: ");
            nazwa = sc.next();
            if (nazwa.equals("6")) {
                cls();
                return;
            }
        } while (p > 0);
        if (otwartyPlik.equals(nazwa)) {
            System.out.println("Dany plik jest aktualnie otwarty czy kontynuowac?\n Jesli nie, wpisz n\nJesli chcesz kontynuowac, wpisz T\n");
            n = sc.next().charAt(0);
            if (n == 'n' || n == 'N') {
                cls();
                return;
            } else if(n == '1'){
                otwartyPlik = "Brak";
                Files.delete(Paths.get(nazwa));
                cls();
                System.out.println("Plik został usunięty");
                return;
            }
        } else {
            Files.delete(Paths.get(nazwa));
            cls();
            System.out.println("Plik został usunięty");
            return;
        }
    }
    static boolean sprawdzIstnienie(String nazwa) {
        boolean poprawne = true;
        String baza = "bazaxx.dat";
        int dlugosc = nazwa.length();
        if (dlugosc != 10) {
            poprawne = false;
            System.out.println("Nazwa ma 10 znakow!");
        }else
            for (int i = 0; i < dlugosc; i++) {
                if (i < 4 || i > 5) {
                    if (nazwa.charAt(i) != baza.charAt(i)) {
                        poprawne = false;
                        System.out.println("Nazwa musi nosic nazwe bazaXX.dat (gdzie XX to liczby z zakresu od 1 do 99)");
                        break;
                    }
                }else if(!Character.isDigit(nazwa.charAt(i))) {
                    poprawne = false;
                    System.out.println("Nazwa musi nosic nazwe bazaXX.dat (gdzie XX to liczby z zakresu od 1 do 99)");
                    break;
                }
            }
        return poprawne;
    }



    static int poz = 0;
    public static void przegladBazy() throws IOException {
        char ch;
        Dane temp;
        if(otwartyPlik.equals("brak")) {
            System.out.println("Zaden plik nie jest otwarty");
            System.out.println("\n");
            return;
        }

        if (list.isEmpty()) {
            System.out.println("Tablica jest pusta. Zanim bedziesz mogl korzystac z tej funkcji, dodamy jeden wiersz do bazy danych.");
            list.add(poz,wczytajStrukt());
        }

        temp = list.get(poz);
        System.out.println("\n\n\nStruktura\n");
        drukujStrukt(temp);
        drukujOpcje();

        System.out.println("\n\n Wybierz opcje: \n");
        do{
            ch =  (char) System.in.read();
        }while(ch != 'U' && ch != 'D' && ch != 'M' && ch != 'Y' && ch != 'G' && ch != 'H' && ch != 'B' && ch!= 'X' && ch != 'u' && ch != 'd' && ch != 'm' && ch != 'y' && ch != 'g' && ch != 'h' && ch != 'b' && ch != 'x');

        switch(ch){
            case 'B': case 'b':
                if(poz>0)
                    poz--;
                else {
                    cls();
                    System.out.println("Nie mozna przesunac do tylu, gdyz jestes na poczatku");
                }
                przegladBazy();
                break;
            case 'Y': case 'y':
                if(poz<list.size()-1)
                    poz++;
                else {
                    cls();
                    System.out.println("Nie mozna przesunac do tylu, gdyz jestes na koncu");
                }
                przegladBazy();
                break;
            case 'G': case 'g':
                poz=0;
                cls();
                przegladBazy();
                break;
            case 'H': case 'h':
                poz = list.size()-1;
                cls();
                przegladBazy();
                break;
            case 'D': case'd':
                cls();
                list.add(++poz,wczytajStrukt());
                zapisz();
                cls();
                przegladBazy();
                break;
            case 'U': case'u':
                list.remove(poz);
                zapisz();
                cls();
                przegladBazy();
                break;
            case 'M': case'm':
                cls();
                modyfikuj(temp);
                list.set(poz, temp);
                zapisz();
                cls();
                przegladBazy();
                break;
            case 'X': case 'x':
                System.out.println("\nWyjscie do menu");
                poz=0;
                zapisz();
                cls();
                return;
        }
        cls();
        return;
    }

    static void drukujStrukt(Dane dane) {
        System.out.println("Nr wiersza: "+(poz+1));
        System.out.println("ID: " + dane.id);
        System.out.println("Nadawca: " + dane.nadawca);
        System.out.println("Odbiorca: " + dane.odbiorca);
        System.out.println("Data: " + dane.data);
        System.out.println("Tresc: " + dane.tresc);
        System.out.println("\n");
    }

    static void drukujOpcje(){
        System.out.print(" B - Przesun o jeden do tylu   |");
        System.out.print(" Y - Przesun o jeden do przodu   |");
        System.out.print(" G - Przesun na poczatek   |");
        System.out.print(" H - Przesun na koniec   |");
        System.out.print(" D - Dopisz strukture   |");
        System.out.print(" U - Usun biezaca strukture   |");
        System.out.print(" M - Modyfikuj biezaca strukture   |");
        System.out.print(" X - Wyjdz z przegladu   |");
    }

    static Dane wczytajStrukt() {
        Scanner sc = new Scanner(System.in);
        Dane temp = new Dane();
        System.out.println("ID: ");
        while(!sc.hasNextInt()) {
            System.out.println("Nie podales prawidlowej liczby, podaj jeszcze raz");
            sc.next();
        }
        temp.id = sc.nextInt();
        System.out.println("Nadawca:");
        while(!sc.hasNextLine()) {
            System.out.println("Nie podales prawidlowach danych, podaj jeszcze raz");
            sc.next();
        }
        temp.nadawca = sc.next();
        System.out.println("Odbiorca:");
        while(!sc.hasNextLine()) {
            System.out.println("Nie podales prawidlowach danych, podaj jeszcze raz");
            sc.next();
        }
        temp.odbiorca = sc.next();
        System.out.println("Data:");
        temp.data = dodajDate();
        System.out.println("Tresc:");
        while(!sc.hasNextLine()) {
            System.out.println("Nie podales prawidlowach danych, podaj jeszcze raz");
            sc.next();
        }
        temp.tresc = sc.next();
        return temp;
    }

    static Date dodajDate(){
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Scanner sc = new Scanner(System.in);
            System.out.println("Wprowadz date w formacie dd/MM/yyyy (dd - dzien; mm - miesiac; yyyy - rok) \n");
            String s = sc.nextLine();
            return dateFormat.parse(s);
        }catch(Exception e){
            System.out.println(e);
        }
        return null;
    }

    static void modyfikuj(Dane temp){
        Scanner sc = new Scanner(System.in);
        int wybor;
        System.out.println("Co chcesz zmienic?");
        System.out.println("1. ID");
        System.out.println("2. Nadawca");
        System.out.println("3. Odbiorca");
        System.out.println("4. Data");
        System.out.println("5. Tresc");
        System.out.println("6. Wyjdz z modyfikacji");

        wybor = sc.nextInt();

        sc.reset();
        switch(wybor){
            case 1:
                System.out.println("Podaj nowa wartosc");
                while(!sc.hasNextInt()) {
                    System.out.println("Nie podales prawidlowej liczby, podaj jeszcze raz");
                    sc.next();
                }
                temp.id = sc.nextInt();
                break;
            case 2:
                System.out.println("Podaj nowa wartosc");
                while(!sc.hasNextLine()) {
                    System.out.println("Nie podales prawidlowach danych, podaj jeszcze raz");
                    sc.next();
                }
                temp.nadawca = sc.next();
                break;
            case 3:
                System.out.println("Podaj nowa wartosc");
                while(!sc.hasNextLine()) {
                    System.out.println("Nie podales prawidlowach danych, podaj jeszcze raz");
                    sc.next();
                }
                temp.odbiorca = sc.next();
                break;
            case 4:
                temp.data=dodajDate();
                break;
            case 5:
                System.out.println("Podaj nowa wartosc");
                while(!sc.hasNextLine()) {
                    System.out.println("Nie podales prawidlowach danych, podaj jeszcze raz");
                    sc.next();
                }
                temp.tresc=sc.next();
                break;
            case 6:
                return;
            default:
                System.out.println("Zostala wybrana zla opcja.");
                modyfikuj(temp);
        }
    }

    public static void sortowanieBazy(){
        Scanner sc = new Scanner(System.in);
        int wybor;
        Comparator<Dane> comperator;
        System.out.println("Co chcesz posortowac?");
        System.out.println("1. ID");
        System.out.println("2. Nadawca");
        System.out.println("3. Odbiorca");
        System.out.println("4. Data");
        System.out.println("5. Tresc");
        System.out.println("6. Wyjdz z modyfikacji");

        wybor = sc.nextInt();

        sc.reset();
        switch(wybor){
            case 1:
                comperator = Comparator.comparing(Dane::getId);
                list.sort(comperator);
                return;
            case 2:
                comperator = Comparator.comparing(Dane::getNadawca);
                list.sort(comperator);
                return;
            case 3:
                comperator = Comparator.comparing(Dane::getOdbiorca);
                list.sort(comperator);
                return;
            case 4:
                comperator = Comparator.comparing(Dane::getData);
                list.sort(comperator);
                return;
            case 5:
                comperator = Comparator.comparing(Dane::getTresc);
                list.sort(comperator);
                return;
            case 6:
                return;
            default:
                System.out.println("Zostala wybrana zla opcja.");
                sortowanieBazy();
        }
    }
    static void zapisz()throws IOException{
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(otwartyPlik));
        out.writeObject(list);
        out.close();
    }
    static void cls(){
        System.out.println(new String(new char[25]).replace("\0","\r\n"));
    }
}