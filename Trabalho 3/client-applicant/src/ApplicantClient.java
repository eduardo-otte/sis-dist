import models.Curriculum;
import models.JobOffering;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Scanner;

public class ApplicantClient {
    public static void main(String[] args) {
        System.out.println("Inicializando cliente de empregado");
        boolean hasCurriculum = false;
        boolean mayEnd = false;
        int option;
        String input;
        
        Scanner keyboard = new Scanner(System.in);
       
        Curriculum curriculum = new Curriculum("", "", "", 0, 0); 
        
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            ServerInterface serverInterface = (ServerInterface) registry.lookup("server");

            ApplicantClientImpl client = new ApplicantClientImpl();

            System.out.println("Cliente inicializado");
            
            while(!mayEnd) {
            	System.out.println("---------------------------");
	            System.out.println("Selecione a opção desejada:");
	            System.out.println("1) Enviar currículo");
	            System.out.println("2) Alterar dados do currículo");
	            System.out.println("3) Checar vagas cadastradas");
	            System.out.println("4) Cadastrar para notificação de novas ofertas de emprego");
	            System.out.println("5) Encerrar");
	            option = Parser.parseIntegerInput("");
	            
	           
	            if(option == 1 && hasCurriculum) {
	            	System.out.println("Você já tem um currículo cadastrado. Se você quiser modificar seus dados, escolha a opção 2");
	            }
	            else if(option == 1 && !hasCurriculum) {
	            	System.out.print("Nome: ");
	            	input = keyboard.nextLine();
	            	curriculum.setName(input);
	            	            	
	            	System.out.print("Contato: ");
	            	input = keyboard.nextLine();
	            	curriculum.setContact(input);
	            	
	            	System.out.print("Área de interesse: ");
	            	input = keyboard.nextLine();
	            	curriculum.setArea(input);

	            	int workload = Parser.parseIntegerInput("Carga horária: ");
	            	curriculum.setWorkload(workload);

	            	float salary = Parser.parseFloatInput("Salário pretendido: ");
	            	curriculum.setIntendedSalary(salary);
	            	            	
	            	hasCurriculum = true;
	                System.out.println("Currículo criado!");
	                serverInterface.registerCurriculum(curriculum);
	            }

				// 2) Alterar dados do curriculo
	            if(option == 2 && !hasCurriculum) {
	            	System.out.println("Você ainda não tem um currículo cadastrado. Se você quiser enviar um, escolha a opção 1");
	            }

	            if(option == 2 && hasCurriculum) {
	            	System.out.println("Seu contato atual é " + curriculum.getContact());
	            	System.out.println("Você deseja modifica-lo? S/N");
	            	input = keyboard.nextLine();
	            	if(input.equals("S")) {
	            		System.out.print("Digite seu novo contato:");
	                	input = keyboard.nextLine();
	                	curriculum.setContact(input);
	            	}
	            	
	            	System.out.println("Sua Área de interesse atual é " + curriculum.getArea());
	            	System.out.println("Você deseja modifica-la? S/N");
	            	input = keyboard.nextLine();
	            	if(input.equals("S")) {
	            		System.out.print("Digite sua nova área de interesse: ");
	                	input = keyboard.nextLine();
	                	curriculum.setArea(input);
	            	}
	            	
	            	System.out.println("Sua carga horária atual é " + curriculum.getWorkload());
	            	System.out.println("Você deseja modifica-la? S/N");
	            	input = keyboard.nextLine();
	            	if(input.equals("S")) {
	                	int newWorkload = Parser.parseIntegerInput("Digite sua nova carga horária: ");
	                	curriculum.setWorkload(newWorkload);
	            	}
	            	
	            	System.out.println("Sua pretenção salarial atual é " + curriculum.getIntendedSalary());
	            	System.out.println("Você deseja modifica-la? S/N");
	            	input = keyboard.nextLine();
	            	if(input.equals("S")) {
	                	float newSalary = Parser.parseFloatInput("Digite sua nova pretenção salarial: ");
	                	curriculum.setIntendedSalary(newSalary);
	            	}
	            	
	            	serverInterface.alterCurriculum(curriculum);
	            	System.out.println("Seu currículo foi alterado");
	            }

				// 3) Checar vagas cadastradas
	            else if (option == 3) {
	            	//Consulta de vagas de emprego, indicando filtros como área de interesse e salário mínimo pretendido
	            	System.out.print("Qual a área de interesse a ser pesquisada? ");
	            	input = keyboard.nextLine();
	            	String area = input;

	            	float salary = Parser.parseFloatInput("Qual a o salário mínimo pretendido? ");
	            	
	            	JobOffering jobOfferingLookup = new JobOffering("", "", area, 0, salary);
	            	ArrayList<JobOffering> jobsAvaliable = serverInterface.searchJobOffering(jobOfferingLookup);
	            	
	            	if(jobsAvaliable.isEmpty()) {
	            		System.out.println("Não há vagas disponíveis que se encaixam nos padrões da pesquisa. Se você quiser se inscrever para receber vagas futuras, escolha a opção 4");
	            	}
	            	else {
	            		System.out.println("\nVagas encontradas: " + jobsAvaliable.size());
	            		for (int i = 0; i < jobsAvaliable.size(); i++) {
	            			System.out.println("Vaga de número " + (i+1));
	            			System.out.println(" - Nome da empresa: " + jobsAvaliable.get(i).getCompanyName());
	            			System.out.println(" - Contato: " + jobsAvaliable.get(i).getContact());
	            			System.out.println(" - Área: " + jobsAvaliable.get(i).getArea());
	            			System.out.println(" - Carga horária: " + jobsAvaliable.get(i).getWorkload());
	            			System.out.println(" - Salário: " + jobsAvaliable.get(i).getSalary());
	            		}
	            		
	            	}
	            }

				// 4) Cadastrar para envio de novas ofertas de emprego
	            else if(option == 4) {
	            	System.out.print("Qual a área de interesse que você gostaria de cadastrar? ");
	            	input = keyboard.nextLine();
	            	String areaOfInterest = input;
	            	serverInterface.subscribeForJobOfferings(client, areaOfInterest);
	            }
	            else if(option==5) {
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
