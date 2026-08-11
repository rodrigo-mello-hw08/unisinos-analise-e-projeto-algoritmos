#include <stdio.h>
#include <vector>
#include <random>
#include <iostream>
#include <chrono>

using namespace std;


std::mt19937& GetGenerator() {
    static std::mt19937 gen(std::random_device{}());
    return gen;
}


std::vector<int> CreateRandomWithDuplicates(int size, int maxValue) {
    std::vector<int> result(size);
    auto gen = GetGenerator();
    
    for (int& val : result) {
        val = (gen() % maxValue) + 1;
    }

    return result;
}


int COMP_QTD = 0;

void conquer(std::vector<int> &a, std::vector<int> &aux, int low, int middle, int high) {
    
    COMP_QTD++;
    for (int k = low; k <= high; k++) {
        COMP_QTD++;
        aux[k] = a[k];
    }
    
    int i = low;
    int j = middle + 1;
    
    COMP_QTD++;
    for (int k = low; k <= high; k++) {
        COMP_QTD++;
        if (i > middle) {
            COMP_QTD++;
            a[k] = aux[j++];
        } else if (j > high) {
            COMP_QTD++;
            a[k] = aux[i++];
        } else if (aux[j] < aux[i]) {
            COMP_QTD++;
            a[k] = aux[j++];
        } else {
            a[k] = aux[i++];
        }
    }
    
}

void divide(std::vector<int> &a, std::vector<int> &aux, int low, int high) {
    if (low >= high) { 
        COMP_QTD++;
        return;
    }

    int middle = (low + high) / 2;
    divide(a, aux, low, middle);
    divide(a, aux, middle + 1, high);
    conquer(a, aux, low, middle, high);
}



void sort(std::vector<int>& nums) {
    int n = nums.size();
    std::vector<int> aux(n);
    divide(nums, aux, 0, n - 1);
}

vector<bool> sieve(int n) {
    vector<bool> is_prime(n + 1, true);
    is_prime[0] = is_prime[1] = false;
    
    for (int p = 2; p * p <= n; p++) {
        if (is_prime[p]) {
            for (int i = p * p; i <= n; i += p) {
                is_prime[i] = false;
            }
        }
    }
    return is_prime;
}


constexpr int MAX_N = 100000;
constexpr int MAX_I = 15000;
vector<bool> is_prime;

void calc_primes(vector<int>& vec, vector<int>& ans){

    for(int i : vec){
        if(is_prime[i]) ans.push_back(i);
    }
}

void print_vec(vector<int>& vec){
    for(int i : vec) cout << i << "\n";
}

struct Timing
{
    std::chrono::steady_clock::time_point inicio, fim;

    void start() {
        inicio = std::chrono::steady_clock::now();
    }
    
    void stop() {
        fim = std::chrono::steady_clock::now();
    }

    double getTime(){
        std::chrono::duration<double, std::milli> duracao = fim - inicio;
        return duracao.count();
    }
};

int main()
{
    is_prime = sieve(MAX_I);
    vector<int> vec;
    vector<int> ans1; ans1.reserve(MAX_N);
    vector<int> ans2; ans2.reserve(MAX_N);

    Timing geral, gerar, ordenar, primos, ordenar_ordenado, primos_ordenado;
    int qtd_primos = 0, qtd_primos_ordenado = 0;
    int qtd_comp = 0, qtd_comp_ordenado = 0;

    geral.start();
    gerar.start();
    vec = CreateRandomWithDuplicates(MAX_N,MAX_I);
    gerar.stop();
    
    primos.start();
    calc_primes(vec,ans1);
    primos.stop();
    
    qtd_primos = ans1.size();
    COMP_QTD = 0;
    
    ordenar.start();
    sort(vec);
    ordenar.stop();
    qtd_comp = COMP_QTD;
    
    primos_ordenado.start();
    calc_primes(vec,ans2);
    primos_ordenado.stop();
    
    qtd_primos_ordenado = ans2.size();
    
    COMP_QTD = 0;
    ordenar_ordenado.start();
    sort(vec);
    ordenar_ordenado.stop();
    qtd_comp_ordenado = COMP_QTD;
    
    geral.stop();

    std::cout << "Tempo geral                    : " << geral.getTime() << " ms\n";
    std::cout << "Tempo gerar                    : " << gerar.getTime() << " ms\n";
    std::cout << "Tempo ordenar                  : " << ordenar.getTime() << " ms\n";
    std::cout << "Tempo primos                   : " << primos.getTime() << " ms\n";
    std::cout << "Tempo ordenar_ordenado         : " << ordenar_ordenado.getTime() << " ms\n";
    std::cout << "Tempo primos_ordenado          : " << primos_ordenado.getTime() << " ms\n";
    std::cout << "Quantidade comparacoes         : " << qtd_comp << "\n";
    std::cout << "Quantidade comparacoes ordenado: " << qtd_comp_ordenado << "\n";
    std::cout << "Quantidade primos              : " << qtd_primos << "\n";
    std::cout << "Quantidade primos ordenado     : " << qtd_primos_ordenado << "\n";
    
    
    return 0;
}