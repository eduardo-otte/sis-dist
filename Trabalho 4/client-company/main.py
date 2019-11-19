import rest_operations as ro
from types import *

url = 'http://localhost:3000'



def printCurriculums(curriculums):
    i = 1
    for curriculum in curriculums:
        print()
        print("Vaga: " + str(i))
        print(" - Nome: " + curriculum['name'])
        print(" - Área: " + curriculum['area'])
        print(" - Contato: " + curriculum['contact'])
        print(" - Salário Pretendido: " + str(curriculum['intendedSalary']))
        print(" - Carga horária: " + str(curriculum['workload']))
        i += 1


def main():
    selectedOption = 0
    hasJobOffers = False
    jobOffers = []
    jobOffersCounter = 0

    while(selectedOption != 4):
        print("---------------------------")
        print("Selecione a opção desejada:")
        print("1) Enviar Oferta de emprego")
        print("2) Alterar dados da oferta de emprego")
        print("3) Checar currúculos cadastradas")
        print("4) Encerrar")

        selectedOption = int(input())

        #1) Enviar Oferta de emprego
        if (selectedOption == 1):
            jobOffer = {}
            #area, companyName, contact, salary, workload
            companyName = input("Nome da Companhia: ")
            area = input("Área de Interesse: ")
            contact = input("Contato: ")
            salary = float(input("Salário: "))
            workload = int(input("Carga horária: "))

            jobOffer['companyName'] = companyName
            jobOffer['area'] = area
            jobOffer['contact'] = contact
            jobOffer['salary'] = salary
            jobOffer['workload'] = workload

            jobOffers.append(ro.register('jobOffer', jobOffer))
            hasJobOffers = True
            jobOffersCounter+=1

        #2) Alterar dados da Oferta de emprego
        elif (selectedOption == 2) and (not hasJobOffers):
            print("Você não possui ofertas de emprego cadastrado. Para fazer um cadastro, selecione a opção 1")

        elif (selectedOption == 2) and hasJobOffers:

            index = 0

            if(jobOffersCounter>1):
                print("Você tem "+ jobOffersCounter + " ofertas. Selecione qual você deseja alterar ")
                for i in range(jobOffersCounter):
                    print("Vaga " + str(i))
                    print(" - Área " + jobOffers[i]['area']);
                    print(" - Contato " + jobOffers[i]['contact']);
                    print(" - Salário " + str(jobOffers[i]['salary']));
                index = int(input())

            jobOffer = { }

            userInput = input("Você gostaria de atualizar a área de interesse? (S/N)? ")
            if userInput.lower() == 's':
                jobOffer['area'] = int(input("área de interesse: "))

            userInput = input("Você gostaria de atualizar seu contato (S/N)? ")
            if userInput.lower() == 's':
                jobOffer['contact'] = input("Contato: ")

            userInput = input("Você gostaria de atualizar seu salário pretendido (S/N)? ")
            if userInput.lower() == 's':
                jobOffer['intendedSalary'] = float(input("Salário pretendido: "))

            jobOffer['id'] = jobOffers[index]['id']

            if(jobOffer):
                jobOffer['id'] = jobOffers[index]
                ro.update('jobOffer', jobOffer)

        #3) Checar curriculos cadastradas
        elif (selectedOption == 3):
            #Consulta de vagas de emprego, indicando filtros como área de interesse

            curriculumWanted = { }

            userInput = input("Qual a área procurada? ")
            curriculumWanted['area'] = userInput

            if(curriculumWanted):
                response = ro.get('curriculum', curriculumWanted)

                if response["status_code"] == 400:
                    print("Parâmetros de pesquisa incorretos")
                elif response["status_code"] == 404:
                    print("Nenhum currúculo encontrado com os filtros desejados")
                elif response["status_code"] == 200:
                    printCurriculums(response["body"])
                else:
                    print("A pesquisa retornou erro com status: " + response["status_code"])


if __name__ == "__main__":
    main()
