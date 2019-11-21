import rest_operations as ro

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

def getCompanyOffers(companyName):
    searchFilter = {
        "companyName": companyName
    }

    response = ro.get("jobOffer", searchFilter)

    if 'body' in response:
        print("Foram encontradas " + str(len(response['body'])) +
            " ofertas de emprego no nome de sua companhia")

        return response['body']

    else:
        print("Nenhuma oferta de emprego foi encontrada em nome de sua companhia. Que tal cadastrar uma?")

def main():
    selectedOption = 0
    hasJobOffers = False

    companyName = input("Insira o nome de sua companhia: ")
    jobOffers = getCompanyOffers(companyName)
    jobOffersCounter = 0

    if jobOffers:
        jobOffersCounter = len(jobOffers)
        hasJobOffers = True
    else:
        jobOffers = []

    while(selectedOption != 4):
        print()
        print("---------------------------")
        print("Selecione a opção desejada:")
        print("1) Enviar Oferta de emprego")
        print("2) Alterar dados da oferta de emprego")
        print("3) Checar currúculos cadastrados")
        print("4) Encerrar")

        selectedOption = int(input())

        #1) Enviar Oferta de emprego
        if (selectedOption == 1):
            print()
            print("Cadastro de oferta de emprego")

            jobOffer = {}

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
            jobOffersCounter += 1

        #2) Alterar dados da Oferta de emprego
        elif (selectedOption == 2) and (not hasJobOffers):
            print("Você não possui ofertas de emprego cadastrado. Para fazer um cadastro, selecione a opção 1")

        elif (selectedOption == 2) and hasJobOffers:
            print()
            print("Atualização de oferta de emprego")

            index = 0

            if(jobOffersCounter > 1):
                print("Você tem " + str(jobOffersCounter) + " ofertas de emprego cadastradas. Selecione qual você deseja alterar ")
                for i in range(jobOffersCounter):
                    print()
                    print("Vaga " + str(i + 1))
                    print(" - Área " + jobOffers[i]['area'])
                    print(" - Contato " + jobOffers[i]['contact'])
                    print(" - Salário " + str(jobOffers[i]['salary']))
                index = int(input()) - 1

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
                updatedJobOffer = ro.update('jobOffer', jobOffer)
                jobOffers[index] = updatedJobOffer

        #3) Checar curriculos cadastradas
        elif (selectedOption == 3):
            #Consulta de vagas de emprego, indicando filtros como área de interesse

            print()
            print("consulta de currículos")

            curriculumWanted = { }

            userInput = input("Qual a área procurada? ")
            curriculumWanted['area'] = userInput

            if(curriculumWanted):
                response = ro.get('curriculum', curriculumWanted)

                if response["status_code"] == 400:
                    print("Parâmetros de pesquisa incorretos")
                elif response["status_code"] == 404:
                    print("Nenhum currículo encontrado com os filtros desejados")
                elif response["status_code"] == 200:
                    printCurriculums(response["body"])
                else:
                    print("A pesquisa retornou erro com status: " + response["status_code"])


if __name__ == "__main__":
    main()
