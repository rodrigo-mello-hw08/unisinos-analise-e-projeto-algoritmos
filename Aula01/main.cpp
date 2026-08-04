#include <stdio.h>
#include <vector>
#include <random>
#include <iostream>

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


void conquer(std::vector<int> &a, std::vector<int> &aux, int low, int middle, int high) {
    for (int k = low; k <= high; k++) aux[k] = a[k];

    int i = low;
    int j = middle + 1;

    for (int k = low; k <= high; k++) {
        if      (i > middle)       a[k] = aux[j++];
        else if (j > high)         a[k] = aux[i++];
        else if (aux[j] < aux[i])  a[k] = aux[j++];
        else                       a[k] = aux[i++];
    }
    
}

void divide(std::vector<int> &a, std::vector<int> &aux, int low, int high) {
    if (low >= high) return;

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


constexpr MAX_N = 10000;
constexpr MAX_I = 15000;

void print_primes(vector<int>& vec){
    static vector<bool> is_prime = sieve(MAX_I);   
    
    for(int i : vec){
        if(is_prime[i]) cout << i << "\n";
    }
    
}

void print_vec(vector<int>& vec){
    for(int i : vec) cout << i << "\n";
}

int main()
{
    vector<int> vec = CreateRandomWithDuplicates(MAX_N,MAX_I);
    
    int running = true;
    while(running){

        cout << "\nSelecione uma opção:" << endl;   
        cout << "1 - Gerar um Vetor novo" << endl;   
        cout << "2 - Exibir o Vetor Atual" << endl;   
        cout << "3 - Ordenar o Vetor Atual" << endl;   
        cout << "4 - Exibir primos do Vetor Atual" << endl;   
        cout << "0 - Sair" << endl;
        cout << "> ";
        
        int choice;
        cin >> choice;
        
        switch(choice){
            case 0:
                running = false;
                break;
            case 1:
                vec = CreateRandomWithDuplicates(MAX_N,MAX_I);
                break;
            case 2:
                print_vec(vec);
                break;
            case 3:
                sort(vec);
                break;
            case 4:
                print_primes(vec);
                break;
        }
    }
    return 0;
}