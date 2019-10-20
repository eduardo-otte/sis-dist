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
        int option=-1;
        String input = new String(); 
        
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
	            System.out.println("1)Enviar currículo");
	            System.out.println("2)Alterar dados do currículo");
	            System.out.println("3)Checar vagas cadastradas");
	            System.out.println("4)Cadastrar para envio de novas ofertas de emprego");
	            System.out.println("5)Encerrar");
	            option = keyboard.nextInt();
	            
	           
	            if(option==1 && hasCurriculum) {
	            	System.out.println("Você já tem um currículo cadastrado.Se você quiser modificar seus dados, escolha a opção 2");
	            }
	            else if(option==1 && !hasCurriculum) {
	            	System.out.println("Nome:");
	            	input = keyboard.next();
	            	curriculum.setName(input);
	            	            	
	            	System.out.println("Contato:");
	            	input = keyboard.next();
	            	curriculum.setContact(input);
	            	
	            	System.out.println("Área de interesse:");
	            	input = keyboard.next();
	            	curriculum.setArea(input);
	            	
	            	System.out.println("Carga horária:");
	            	input = keyboard.next();
	            	curriculum.setWorkload(Integer.parseInt(input));
	            	
	            	System.out.println("Salário pretendido:");
	            	input = keyboard.next();
	            	curriculum.setIntendedSalary(Float.parseFloat(input));            	
	            	            	
	            	hasCurriculum=true;
	                System.out.println("Currículo criado!");
	                serverInterface.registerCurriculum(curriculum);
	            }
	            if(option==2 && !hasCurriculum) { //alterar dados do curriculo
	            	System.out.println("Você ainda não tem um currículo cadastrado. Se você quiser enviar um, escolha a opção 1");
	            }
	            if(option==2 && hasCurriculum) {
	            	System.out.println("Seu contato atual é "+ curriculum.getContact());
	            	System.out.println("Você deseja modifica-lo? S/N");
	            	input = keyboard.next().trim();
	            	if(input.equals("S")) {
	            		System.out.println("Digite seu novo contato:");
	                	input = keyboard.next();
	                	curriculum.setContact(input);
	            	}
	            	
	            	System.out.println("Sua Área de interesse atual é "+ curriculum.getArea());
	            	System.out.println("Você deseja modifica-la? S/N");
	            	input = keyboard.next().trim();
	            	if(input.equals("S")) {
	            		System.out.println("Digite sua nova área de interesse:");
	                	input = keyboard.next();
	                	curriculum.setArea(input);
	            	}
	            	
	            	System.out.println("Sua carga horária atual é "+ curriculum.getWorkload());
	            	System.out.println("Você deseja modifica-la? S/N");
	            	input = keyboard.next().trim();
	            	if(input.equals("S")) {
	            		System.out.println("Digite sua nova carga horária? S/N");
	                	input = keyboard.next();
	                	curriculum.setWorkload(Integer.parseInt(input));
	            	}
	            	
	            	System.out.println("Sua pretenção salarial atual é "+ curriculum.getIntendedSalary());
	            	System.out.println("Você deseja modifica-lo? S/N");
	            	input = keyboard.next().trim();
	            	if(input.equals("S")) {
	            		System.out.println("Digite sua nova pretenção salarial? S/N");
	                	input = keyboard.next();
	                	curriculum.setIntendedSalary(Float.parseFloat(input));      
	            	}
	            	
	            	serverInterface.alterCurriculum(curriculum);
	            	System.out.println("Seu currículo foi alterado");
	            }
	            else if (option==3) {	//3)Checar vagas cadastradas
	            	//Consulta de vagas de emprego, indicando filtros como área de interesse e salário mínimo pretendido
	            	System.out.println("Qual a área de interesse a ser pesquisada?");
	            	input = keyboard.next();
	            	String area = input;
	            	//System.out.println("Qual a carga horária pretendida?");
	            	//input = keyboard.next();
	            	//int workload = Integer.parseInt(input);
	            	System.out.println("Qual a o salário mínimo pretendido?");
	            	input = keyboard.next();
	            	float salary = Float.parseFloat(input);
	            	
	            	JobOffering jobOfferingLookup = new JobOffering("", "", area, 0, salary);
	            	ArrayList<JobOffering> jobsAvaliable = serverInterface.searchJobOffering(jobOfferingLookup);
	            	
	            	if(jobsAvaliable.isEmpty()) {
	            		System.out.println("Não há vagas disponíveis que se encaixam nos padrões da pesquisa. Se você quiser se inscrever para receber vagas futuras, escolha a opção 4");
	            	}
	            	else {
	            		for (int i=0; i<jobsAvaliable.size(); i++) {
	            			System.out.println("Vaga de número " + (i+1));
	            			System.out.println("Nome da empresa: " + jobsAvaliable.get(i).getCompanyName());
	            			System.out.println("Contato: " + jobsAvaliable.get(i).getContact());
	            			System.out.println("Área: " + jobsAvaliable.get(i).getArea());
	            			System.out.println("Carga horária: " + jobsAvaliable.get(i).getWorkload());
	            			System.out.println("Salário: " + jobsAvaliable.get(i).getSalary());
	            		}
	            		
	            	}
	            }
	            else if(option==4) {	//4)Cadastrar para envio de novas ofertas de emprego
	            	System.out.println("Qual a área de interesse que você gostaria de cadastrar?");
	            	input = keyboard.next();
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
