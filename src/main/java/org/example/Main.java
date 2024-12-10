package org.example;

import org.example.entities.Client;
import org.example.entities.Ticket;
import org.example.exceptions.InvalidTicketNumberException;
import org.example.exceptions.InvalidTicketTypeException;
import org.hibernate.Session;

import java.util.*;

public class Main {
    private static Session session = null;

    public static void main(String[] args) {
        try {
            session = HibernateUtil.getSession();


            //deleteUser(29);
            // readUser(5);
            ticketsFromOneCarnivalRow("A");
            //updateTicketFromUser(13, "Andrei");
            // changeTicket(13);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            HibernateUtil.closeSession(session);
        }
    }

    // Andrei aqui sinceramente se me fue un poco
    // la cabeza pero ya como lo he hecho me da cosa borrarlo
    public static void creatRegister(String name, int age) {
        session.beginTransaction();
        // creamos cliente
        Client client = createClient(name, age);
        // asignamos sus tickets mediante el metodo createTickets
        Set<Ticket> ticketSet = new HashSet<>(createTickets(client));
        client.setTickets(ticketSet);

        session.persist(client);
        session.getTransaction().commit();

    }

    public static Client createClient(String name, int edad) {
        Client client = new Client();
        client.setName(name);
        client.setAge(edad);
        return client;
    }

    public static Set<Ticket> createTickets(Client client) {
        // metodo que indica las vueltas del for
        int nTickets = numberOfTickets();

        ArrayList<Ticket> ticketList = new ArrayList<>();
        // Bucle que crea n tickets
        for (int i = 0; i < nTickets; i++) {
            Ticket ticket = createOneTicket(client);
            ticketList.add(ticket);
        }

        return new HashSet<>(ticketList);

    }

    public static int numberOfTickets() {
        System.out.println("as");
        Scanner sc = new Scanner(System.in);
        int nTickets=0;
        Boolean incorrectValue=true;

        // se comprueba que no se introduzca numeros mayor a 2 o menores a 1
        while (incorrectValue){
            System.out.print("¿Cuantos tickets quiere, 1 o 2? ");
            try{
                InvalidTicketNumberException check=new InvalidTicketNumberException(" ");
                nTickets=sc.nextInt();
                nTickets=check.validateTicketNumber(nTickets);
                incorrectValue=false;
            } catch (InputMismatchException e){
                System.out.println("El valor introducido no es válido, por favor, introduzca un numerín");
                sc.nextLine();
            } catch (InvalidTicketNumberException e){
                System.out.println(e.getMessage());
            }

        }
        System.out.println(nTickets);
        return nTickets;
    }



    public static Ticket createOneTicket(Client client) {
        Scanner sc = new Scanner(System.in);
        Ticket ticket = new Ticket();
        ticket.setUser(client);
        // controla que solo se elija opcion A o B
        boolean registerCondition = true;

        while (registerCondition) {
            try{
                System.out.println("¿Qué ticket quiere, tipo A o tipo B?");
                String nameTicket = sc.next();
                // Valida si el ticket es a o b si no la lia
                InvalidTicketTypeException check=new InvalidTicketTypeException(" ");
                nameTicket= check.validateTypeTicket(nameTicket);

                if (nameTicket.equalsIgnoreCase("A")) {
                    ticket.setPrice(45.0);
                    ticket.setCarnivalrow("Montaña Rusa");
                    ticket.setName("A");
                    registerCondition = false;
                } else if (nameTicket.equalsIgnoreCase("B")) {
                    ticket.setPrice(35.0);
                    ticket.setCarnivalrow("Resto atracciones");
                    ticket.setName("B");
                    registerCondition = false;
                }
            } catch (InputMismatchException e){
                System.out.println("Ticket A o ticket B, por favor.");
                sc.nextLine();
            } catch (InvalidTicketTypeException e){
                System.out.println(e.getMessage());
                sc.nextLine();
            }
        }

        return ticket;
    }


    public static Client readUser(int userId) {
        String HQL = "FROM Client WHERE id=:userId";

        Client client = session.createQuery(HQL, Client.class)
                .setParameter("userId", userId)
                .uniqueResult();
        if (client.getId() != null) {
            System.out.println("ID " + client.getId() + " Nombre " + client.getName());
        }
        return client;
    }

    public static void readTicket(int id) {
        String HQL = "FROM Ticket WHERE id=:id";

        Ticket ticket = session.createQuery(HQL, Ticket.class)
                .setParameter("id", id)
                .uniqueResult();
        if (ticket.getId() != null) {
            System.out.println("ID " + ticket.getId() + " Nombre " + ticket.getName());
        }
    }

    // Con este método recapitulo la información total de un usuario
    // primero llamo al metodo readUser que nos devuelve un objeto client
    // estee objeto client nos permite tener el nombre del usuario y luego
    // nos permite buscar sus tickets en la BBDD
    public static void collectUserData(int id) {
        Client client = readUser(id);

        String HQL = "FROM Ticket WHERE user=:client";

        List<Ticket> ticketList = session.createQuery(HQL, Ticket.class).setParameter("client", client).list();
        System.out.print("El cliente llamado " + client.getName() + " con id " + client.getId() + " posee un ");

        for (Ticket ticket : ticketList) {
            System.out.println(ticket.getName() + " correspondiente a " + ticket.getCarnivalrow());
        }
    }

    public static void deleteUser(int id) {
        Client client = session.get(Client.class, id);

        if (client != null) {
            session.beginTransaction();
            session.remove(client);
            session.getTransaction().commit();
            System.out.println("Usuario con id " + client.getId() + " elimnado correctamente");
        } else {
            System.out.println("No se pudo eliminar el usuario");
        }
    }

    public static void deleteTicketFromUser(int id) {
        // Leo el cliente
        session.beginTransaction();
        Client client = readUser(id);
        //Almaceno sus tickets
        List<Ticket> ticketList = new ArrayList<>(client.getTickets());
        System.out.println("¿Qué ticket quiere eliminar A, B o los 2?");
        String answerSelected = ticketSelected();

        // Busco el ticket a elimnar
        if (!answerSelected.equals("2")) {
            for (Ticket ticket : ticketList) {
                if (ticket.getName().equalsIgnoreCase(answerSelected)){
                    client.getTickets().remove(ticket);
                    session.remove(ticket);
                    System.out.println("Elimnando ticket "+ticket.getName());
                    if (client.getTickets().isEmpty()){
                        session.remove(client);
                        System.out.println("ELIMInando tickets y cliente");
                    }
                }
             }

        }
        session.getTransaction().commit();

    }

    public static String ticketSelected() {
        Scanner sc = new Scanner(System.in);
        String answer = sc.next();
        String answerSelected = "";
        boolean checkCondition = true;
        try{
            while (checkCondition) {
                if (answer.equalsIgnoreCase("A")) {
                    answerSelected = "A";
                    checkCondition = false;
                } else if (answer.equalsIgnoreCase("B")) {
                    answerSelected = "B";
                    checkCondition = false;
                } else if (answer.equalsIgnoreCase("2")) {
                    answerSelected = "2";
                    checkCondition = false;
                } else {
                    System.out.println("Valor no valido");
                    sc.nextLine();
                }
            }
        } catch (InputMismatchException e){
            System.out.println("Elija un ticket o A o B");

        }
        return answerSelected;
    }



    public static void updatenNameFromUser(int id, String name) {
        Client client = session.get(Client.class, id);
        if (client != null) {
            client.setName(name);

            session.beginTransaction();
            session.merge(client);
            session.getTransaction().commit();

            System.out.println("Cliente actualizado correctamente.");

        } else {
            System.out.println("No se encuetra en la BBDD el usuario");
        }

    }



        public static void ticketsFromOneCarnivalRow(String name){
            String HQL="FROM Ticket where name=:name";
            List<Ticket> ticketList = session.createQuery(HQL, Ticket.class)
                    .setParameter("name", name) // Aquí se asigna el valor al parámetro
                    .list();
            System.out.println("Para la atracción "+name+ " tenemos un total de "+(ticketList.size())+" entradas");
        }


    public static double averageSpendFromUsers(){
        String HQL="FROM Client";
        List<Client> clientList = session.createQuery(HQL, Client.class).list();
        double totalCount=0;
        double counter=0;
        for (Client client:clientList){
            for (Ticket ticket: client.getTickets()){
                totalCount=totalCount+ ticket.getPrice();
            }
            counter++;
        }
        return totalCount/counter;
    }
    public static double totalSpendFromUsers(){
        String HQL="FROM Client";
        List<Client> clientList = session.createQuery(HQL, Client.class).list();
        double totalCount=0;

        for (Client client:clientList){
            for (Ticket ticket: client.getTickets()){
                totalCount=totalCount+ ticket.getPrice();
            }
        }
        return totalCount;
    }
    public static double averageAgeOfUsers(){
        String HQL="FROM Client";
        List<Client> clientList = session.createQuery(HQL, Client.class).list();
        double totalCount=0;
        double counter=0.0;
        for (Client client:clientList){
           totalCount=client.getAge()+totalCount;
           counter++;
        }
        return totalCount/counter;
    }
  }
