import models.Curriculum;
import models.JobOffering;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Scanner;

public class CompanyClient {
    public static void main(String[] args) {
        System.out.println("Inicializando cliente de companhia");

        boolean hasJobOffer = false;
        boolean mayEnd = false;
        int option;
        String input;
        
        Scanner keyboard = new Scanner(System.in);
        
        ArrayList<JobOffering> jobsOffered = new ArrayList<>();
        
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            ServerInterface serverInterface = (ServerInterface) registry.lookup("server");

            CompanyClientImpl client = new CompanyClientImpl();

            System.out.println("Cliente inicializado");
            
            while(!mayEnd) {
            	System.out.println("---------------------------");
	            System.out.println("Selecione a opção desejada:");
	            System.out.println("1) Enviar Oferta de emprego");
	            System.out.println("2) Alterar dados de uma oferta de emprego");
	            System.out.println("3) Checar currículos cadastrados");
	            System.out.println("4) Cadastrar para notificação de novos currículos");
	            System.out.println("5) Encerrar");
	            option = Parser.parseIntegerInput("");
	            
	           	// 1) Cadastra uma oferta de emprego
	            if(option == 1 ) {
	            	System.out.print("Nome da empresa: ");
	            	input = keyboard.nextLine();
	            	String companyName = input;
	            	            	
	            	System.out.print("Contato: ");
	            	input = keyboard.nextLine();
	            	String contact = input;
	            	
	            	System.out.print("Área de interesse: ");
	            	input = keyboard.nextLine();
	            	String area = input;

					int workload = Parser.parseIntegerInput("Carga horária: ");

	            	float salary = Parser.parseFloatInput("Salário pretendido: ");
	            	            	
	            	hasJobOffer = true;
	            	JobOffering jobOffering = new JobOffering(companyName, contact, area, workload, salary);
	            	jobsOffered.add(jobOffering);
	                serverInterface.registerJobOffering(jobOffering);
	                System.out.println("Oferta de emprego criada");
	            }

	            if(option == 2 && !hasJobOffer) { //alterar dados de uma oferta de emprego
	            	System.out.println("Você ainda não tem uma oferta de emprego cadastrada. Se você quiser enviar uma, escolha a opção 1");
	            }

	            if(option == 2 && hasJobOffer) {
	            	int index = 0;
	            	if(jobsOffered.size() > 1) {
	            		System.out.println("Você tem " + jobsOffered.size() + " ofertas de emprego cadastradas. Qual você deseja modificaar?");
		            	for (int i = 0; i < jobsOffered.size(); i++) {
		            	    //companyName and workload can't change
		            		System.out.println("Vaga " + (i+1));
		            		System.out.println(" - Área " + jobsOffered.get(i).getArea());
		            		System.out.println(" - Contato " + jobsOffered.get(i).getContact());
		            		System.out.println(" - Salário " + jobsOffered.get(i).getSalary());
			          	}
		            	index = Parser.parseIntegerInput("") - 1;
	            	}
	            	
	            	System.out.println("Seu contato atual é " + jobsOffered.get(index).getContact());
	            	System.out.println("Você deseja modifica-lo? S/N");
	            	input = keyboard.nextLine().trim();
	            	if(input.equals("S")) {
	            		System.out.print("Digite seu novo contato: ");
	                	input = keyboard.nextLine();
	                	jobsOffered.get(index).setContact(input);
	            	}
	            	
	            	System.out.println("Sua Área de interesse atual é " + jobsOffered.get(index).getArea());
	            	System.out.println("Você deseja modifica-la? S/N");
	            	input = keyboard.nextLine().trim();
	            	if(input.equals("S")) {
	            		System.out.print("Digite sua nova área de interesse: ");
	                	input = keyboard.nextLine();
	                	jobsOffered.get(index).setArea(input);
	            	}
	            	
	            	System.out.println("Seu salário atual é " + jobsOffered.get(index).getSalary());
	            	System.out.println("Você deseja modifica-lo? S/N");
	            	input = keyboard.nextLine().trim();
	            	if(input.equals("S")) {
	                	float newSalary = Parser.parseIntegerInput("Digite sua nova pretenção salarial: ");
	                	jobsOffered.get(index).setSalary(newSalary);
	            	}
	            	
	            	serverInterface.alterJobOffering(jobsOffered.get(index));
	            	System.out.println("Sua oferta de emprego foi alterado");
	            }

				// 3) Checar currículos cadastrados
	            else if (option == 3) {
	            	//Consulta de vagas de emprego, indicando filtros como área de interesse
	            	System.out.println("Qual a área de interesse a ser pesquisada?");
	            	input = keyboard.nextLine();
	            	String area = input;
	            	
	            	ArrayList<Curriculum> curriculumAvaliable = serverInterface.searchCurriculum(area);
	            	
	            	if(curriculumAvaliable.isEmpty()) {
	            		System.out.println("Não há currículos que se encaixem nos padrões da pesquisa");
	            		System.out.println("Se você quiser se inscrever para receber vagas futuras, escolha a opção 4");
	            	} else {
	            		for (int i = 0; i < curriculumAvaliable.size(); i++) {
	            			System.out.println("Currículo de número " + (i+1));
	            			System.out.println(" - Nome : " + curriculumAvaliable.get(i).getName());
	            			System.out.println(" - Contato: " + curriculumAvaliable.get(i).getContact());
	            			System.out.println(" - Área: " + curriculumAvaliable.get(i).getArea());
	            			System.out.println(" - Carga horária: " + curriculumAvaliable.get(i).getWorkload());
	            			System.out.println(" - Salário: " + curriculumAvaliable.get(i).getIntendedSalary());
	            		}
	            		
	            	}
	            }

				// 4) Cadastrar para envio de novos currículos
	            else if(option == 4) {
	            	System.out.println("Qual a área de interesse que você gostaria de cadastrar?");
	            	input = keyboard.nextLine();
	            	String jobArea = input;
	            	try {
	            		serverInterface.subscribeForJobApplicants(client, jobArea);
	            	} catch (Exception e) {
	                    System.out.println("Exception em subscribeForJobApplicants: " + e.getMessage());
	                }
	            }

	            else if(option == 5) {
	            	mayEnd = true;
	            }
            }
        } catch (RemoteException e) {
            System.out.println("RemoteException ao iniciar o cliente: " + e.getMessage());
        } catch (NotBoundException e) {
            System.out.println("NotBoundException ao iniciar o cliente: " + e.getMessage());
        }
    }
}
