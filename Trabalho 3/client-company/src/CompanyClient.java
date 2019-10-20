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
        int option=-1;
        String input = new String(); 
        
        Scanner keyboard = new Scanner(System.in);
        
        ArrayList<JobOffering> jobsOffered = new ArrayList<JobOffering>();
        
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            ServerInterface serverInterface = (ServerInterface) registry.lookup("server");

            CompanyClientImpl client = new CompanyClientImpl();

            System.out.println("Cliente inicializado");

            //JobOffering jobOffering = new JobOffering("Blabla", "blablabla", "casa do caralho", 10, 10000);
            //System.out.println("Oferta de emprego criada");
            //serverInterface.registerJobOffering(jobOffering);
            
            while(!mayEnd) {
            	System.out.println("---------------------------");
	            System.out.println("Selecione a opção desejada:");
	            System.out.println("1)Enviar Oferta de emprego");
	            System.out.println("2)Alterar dados de uma oferta de emprego");
	            System.out.println("3)Checar currículos cadastrados");
	            System.out.println("4)Cadastrar para envio de novos currículos");
	            System.out.println("5)Encerrar");
	            option = keyboard.nextInt();
	            
	           
	            if(option==1 ) {
	            	/*
	                this.companyName = companyName; --Key : can't change
	                this.contact = contact;
	                this.area = area;               --Key : can't change
	                this.workload = workload;
	                this.salary = salary;
	                */
	            	System.out.println("Nome:");
	            	input = keyboard.next();
	            	String companyName = input;
	            	            	
	            	System.out.println("Contato:");
	            	input = keyboard.next();
	            	String contact = input;
	            	
	            	System.out.println("Área de interesse:");
	            	input = keyboard.next();
	            	String area = input;
	            	
	            	System.out.println("Carga horária:");
	            	input = keyboard.next();
	            	int workload = Integer.parseInt(input);
	            	
	            	System.out.println("Salário pretendido:");
	            	input = keyboard.next();
	            	float salary = Float.parseFloat(input);            	
	            	            	
	            	hasJobOffer=true;
	            	JobOffering jobOffering = new JobOffering(companyName, contact, area, workload, salary);
	            	jobsOffered.add(jobOffering);
	                serverInterface.registerJobOffering(jobOffering);
	                System.out.println("Oferta de emprego criada");
	                
	            }
	            if(option==2 && !hasJobOffer) { //alterar dados de uma oferta de emprego
	            	System.out.println("Você ainda não tem uma oferta de emprego cadastrada. Se você quiser enviar uma, escolha a opção 1");
	            }
	            if(option==2 && hasJobOffer) {
	            	
	            	int index=0;
	            	if(jobsOffered.size()>1) {
	            		System.out.println("Você tem "+ jobsOffered.size() + " ofertas de emprego cadastradas. Qual você deseja modificaar?");
		            	for (int i=0; i<jobsOffered.size(); i++) {
		            	    //companyName and workload can't change
		            		System.out.println("Vaga "+ (i+1));
		            		System.out.println(" - Área "+ jobsOffered.get(i).getArea());
		            		System.out.println(" - Contato "+ jobsOffered.get(i).getContact());
		            		System.out.println(" - Salário "+ jobsOffered.get(i).getSalary());
			          	}
		            	input = keyboard.next();
		            	index = Integer.parseInt(input);
	            	}
	            	
	            	System.out.println("Seu contato atual é "+ jobsOffered.get(index).getContact());
	            	System.out.println("Você deseja modifica-lo? S/N");
	            	input = keyboard.next().trim();
	            	if(input.equals("S")) {
	            		System.out.println("Digite seu novo contato:");
	                	input = keyboard.next();
	                	jobsOffered.get(index).setContact(input);
	            	}
	            	
	            	System.out.println("Sua Área de interesse atual é "+ jobsOffered.get(index).getArea());
	            	System.out.println("Você deseja modifica-la? S/N");
	            	input = keyboard.next().trim();
	            	if(input.equals("S")) {
	            		System.out.println("Digite sua nova área de interesse:");
	                	input = keyboard.next();
	                	jobsOffered.get(index).setArea(input);
	            	}
	            	
	            	System.out.println("Seu salário atual é "+ jobsOffered.get(index).getSalary());
	            	System.out.println("Você deseja modifica-lo? S/N");
	            	input = keyboard.next().trim();
	            	if(input.equals("S")) {
	            		System.out.println("Digite sua nova pretenção salarial? S/N");
	                	input = keyboard.next();
	                	jobsOffered.get(index).setSalary(Float.parseFloat(input));      
	            	}
	            	
	            	serverInterface.alterJobOffering(jobsOffered.get(index));
	            	System.out.println("Sua oferta de emprego foi alterado");
	            }
	            else if (option==3) {	//3)Checar currículos cadastrados
	            	
	            	//Consulta de vagas de emprego, indicando filtros como área de interesse
	            	System.out.println("Qual a área de interesse a ser pesquisada?");
	            	input = keyboard.next();
	            	String area = input;
	            	
	            	
	            	ArrayList<Curriculum> curriculumAvaliable = serverInterface.searchCurriculum(area);
	            	
	            	if(curriculumAvaliable.isEmpty()) {
	            		System.out.println("Não há currículos que se encaixem nos padrões da pesquisa");
	            		System.out.println("Se você quiser se inscrever para receber vagas futuras, escolha a opção 4");
	            	}
	            	else {
	            		for (int i=0; i<curriculumAvaliable.size(); i++) {
	            			System.out.println("Currículo de número " + (i+1));
	            			System.out.println(" - Nome : " + curriculumAvaliable.get(i).getName());
	            			System.out.println(" - Contato: " + curriculumAvaliable.get(i).getContact());
	            			System.out.println(" - Área: " + curriculumAvaliable.get(i).getArea());
	            			System.out.println(" - Carga horária: " + curriculumAvaliable.get(i).getWorkload());
	            			System.out.println(" - Salário: " + curriculumAvaliable.get(i).getIntendedSalary());
	            		}
	            		
	            	}
	            }
	            else if(option==4) {	//4)Cadastrar para envio de novos currículos
	            	System.out.println("Qual a área de interesse que você gostaria de cadastrar?");
	            	input = keyboard.next();
	            	String jobArea = input;
	            	try {
	            		serverInterface.subscribeForJobApplicants(client, jobArea);
	            	}catch (Exception e) {
	                    System.out.println("Exception em subscribeForJobApplicants: " + e.getMessage());
	                }
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
