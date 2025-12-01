package org;

import org.dao.PacienteDAO;
import org.model.Paciente;

import java.time.LocalDate;

/**
 * Classe Main com demonstração de operações CRUD para Pacientes.
 * Você pode executar este programa ou utilizar a interface Swing (PacienteUISwing)
 * para gerenciar os pacientes de forma interativa.
 */
public class Main {
    public static void main(String[] args) {
        PacienteDAO dao = new PacienteDAO();

        System.out.println("====================================");
        System.out.println("Sistema de Gerenciamento de Pacientes");
        System.out.println("====================================\n");

        // CREATE - Cadastrar Pacientes
        System.out.println("------[ CREATE - Cadastrar Pacientes ] -------");
        Paciente paciente1 = new Paciente();
        paciente1.setNome("Maria Silva");
        paciente1.setCpf("123.456.789-00");
        paciente1.setDataNascimento(LocalDate.of(1990, 5, 15));
        paciente1.setTelefone("62998765432");
        dao.salvarPaciente(paciente1);

        Paciente paciente2 = new Paciente();
        paciente2.setNome("João Santos");
        paciente2.setCpf("987.654.321-00");
        paciente2.setDataNascimento(LocalDate.of(1985, 3, 22));
        paciente2.setTelefone("62987654321");
        dao.salvarPaciente(paciente2);

        Paciente paciente3 = new Paciente();
        paciente3.setNome("Ana Costa");
        paciente3.setCpf("456.789.123-00");
        paciente3.setDataNascimento(LocalDate.of(1995, 7, 10));
        paciente3.setTelefone("62999999999");
        dao.salvarPaciente(paciente3);

        // READ - Listar todos os pacientes
        System.out.println("\n------[ READ - Listar Todos os Pacientes ] -------");
        dao.listarPacientes().forEach(System.out::println);

        // READ - Buscar por ID
        System.out.println("\n------[ READ - Buscar Paciente por ID ] -------");
        Paciente pacienteEncontrado = dao.buscarPacientePorId(1L);
        System.out.println("Paciente encontrado: " + pacienteEncontrado);

        // READ - Buscar por Nome
        System.out.println("\n------[ READ - Buscar Pacientes por Nome ] -------");
        System.out.println("Buscando por 'Maria':");
        dao.buscarPacientePorNome("Maria").forEach(System.out::println);

        // UPDATE - Atualizar Paciente
        System.out.println("\n------[ UPDATE - Atualizar Paciente ] -------");
        if (pacienteEncontrado != null) {
            pacienteEncontrado.setTelefone("62912345678");
            pacienteEncontrado.setCpf("111.222.333-44");
            dao.atualizarPaciente(pacienteEncontrado);
            System.out.println("Paciente atualizado: " + dao.buscarPacientePorId(1L));
        }

        // DELETE - Excluir Paciente
        System.out.println("\n------[ DELETE - Excluir Paciente ] -------");
        Paciente pacienteParaExcluir = dao.buscarPacientePorId(3L);
        if (pacienteParaExcluir != null) {
            System.out.println("Excluindo: " + pacienteParaExcluir);
            dao.excluirPaciente(3L);
        }

        System.out.println("\n------[ Pacientes Restantes ] -------");
        dao.listarPacientes().forEach(System.out::println);

        System.out.println("\n====================================");
        System.out.println("Operações concluídas com sucesso!");
        System.out.println("Arquivo 'pacientes.csv' foi criado/atualizado");
        System.out.println("====================================");
    }
}
