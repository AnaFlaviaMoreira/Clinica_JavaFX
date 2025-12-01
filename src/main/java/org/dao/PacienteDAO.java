package org.dao;

import org.model.Paciente;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class PacienteDAO {

    private static final String CSV_FILE = "pacientes.csv";
    private static final String CSV_HEADER = "id,nome,cpf,dataNascimento,telefone";
    private final Path filePath;

    public PacienteDAO() {
        this.filePath = Paths.get(CSV_FILE);
        inicializarArquivo();
    }

    private void inicializarArquivo() {
        try {
            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
                Files.write(filePath, CSV_HEADER.getBytes());
                Files.write(filePath, "\n".getBytes(), java.nio.file.StandardOpenOption.APPEND);
            }
        } catch (IOException e) {
            System.err.println("Erro ao inicializar arquivo CSV: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void salvarPaciente(Paciente paciente) {
        try {
            if (paciente.getId() == null) {
                paciente.setId(gerarProximoId());
            }
            
            String linha = paciente.getId() + "," + paciente.getNome() + "," + 
                          paciente.getCpf() + "," + paciente.getDataNascimento() + 
                          "," + paciente.getTelefone();
            
            Files.write(filePath, (linha + "\n").getBytes(), java.nio.file.StandardOpenOption.APPEND);
            System.out.println("Paciente salvo com sucesso!");
        } catch (IOException e) {
            System.err.println("Erro ao salvar paciente: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<Paciente> listarPacientes() {
        List<Paciente> pacientes = new ArrayList<>();
        try {
            List<String> linhas = Files.readAllLines(filePath);
            for (int i = 1; i < linhas.size(); i++) {
                Paciente p = parsearLinha(linhas.get(i));
                if (p != null) {
                    pacientes.add(p);
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao listar pacientes: " + e.getMessage());
            e.printStackTrace();
        }
        return pacientes;
    }

    public List<Paciente> buscarPacientePorNome(String nome) {
        return listarPacientes().stream()
                .filter(p -> p.getNome().toLowerCase().contains(nome.toLowerCase()))
                .collect(Collectors.toList());
    }

    public Paciente buscarPacientePorId(Long id) {
        return listarPacientes().stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void atualizarPaciente(Paciente paciente) {
        try {
            List<String> linhas = Files.readAllLines(filePath);
            List<String> novasLinhas = new ArrayList<>();
            novasLinhas.add(CSV_HEADER);
            
            boolean encontrado = false;
            for (int i = 1; i < linhas.size(); i++) {
                String linha = linhas.get(i);
                if (!linha.trim().isEmpty()) {
                    String id = linha.split(",")[0];
                    if (id.equals(paciente.getId().toString())) {
                        novasLinhas.add(paciente.getId() + "," + paciente.getNome() + "," + 
                                       paciente.getCpf() + "," + paciente.getDataNascimento() + 
                                       "," + paciente.getTelefone());
                        encontrado = true;
                    } else {
                        novasLinhas.add(linha);
                    }
                }
            }
            
            if (encontrado) {
                Files.write(filePath, String.join("\n", novasLinhas).getBytes());
                System.out.println("Paciente atualizado com sucesso!");
            } else {
                System.out.println("Paciente não encontrado!");
            }
        } catch (IOException e) {
            System.err.println("Erro ao atualizar paciente: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void excluirPaciente(Long id) {
        try {
            List<String> linhas = Files.readAllLines(filePath);
            List<String> novasLinhas = new ArrayList<>();
            novasLinhas.add(CSV_HEADER);
            
            boolean encontrado = false;
            for (int i = 1; i < linhas.size(); i++) {
                String linha = linhas.get(i);
                if (!linha.trim().isEmpty()) {
                    String idLinha = linha.split(",")[0];
                    if (idLinha.equals(id.toString())) {
                        encontrado = true;
                    } else {
                        novasLinhas.add(linha);
                    }
                }
            }
            
            if (encontrado) {
                Files.write(filePath, String.join("\n", novasLinhas).getBytes());
                System.out.println("Paciente excluído com sucesso!");
            } else {
                System.out.println("Paciente não encontrado!");
            }
        } catch (IOException e) {
            System.err.println("Erro ao excluir paciente: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Paciente parsearLinha(String linha) {
        try {
            if (linha.trim().isEmpty()) {
                return null;
            }
            
            String[] partes = linha.split(",");
            if (partes.length < 5) {
                return null;
            }
            
            Long id = Long.parseLong(partes[0]);
            String nome = partes[1];
            String cpf = partes[2];
            LocalDate dataNascimento = LocalDate.parse(partes[3]);
            String telefone = partes[4];
            
            return new Paciente(id, nome, cpf, dataNascimento, telefone);
        } catch (Exception e) {
            System.err.println("Erro ao parsear linha: " + linha);
            e.printStackTrace();
            return null;
        }
    }

    private Long gerarProximoId() {
        List<Paciente> pacientes = listarPacientes();
        if (pacientes.isEmpty()) {
            return 1L;
        }
        return pacientes.stream()
                .mapToLong(Paciente::getId)
                .max()
                .orElse(0L) + 1;
    }
}
