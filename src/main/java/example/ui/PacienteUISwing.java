package example.ui;

import example.dao.PacienteDAO;
import example.model.Paciente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

/**
 * Interface gráfica usando Swing (nativa do Java, sem dependências externas).
 * Esta versão funciona sem precisar de JavaFX.
 */
public class PacienteUISwing extends JFrame {
    private PacienteDAO dao;
    private JTextField textFieldNome;
    private JTextField textFieldCPF;
    private JTextField textFieldTelefone;
    private JTextField textFieldData;
    private JTextField textFieldBusca;
    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel labelMensagem;

    public PacienteUISwing() {
        dao = new PacienteDAO();
        inicializarUI();
    }

    private void inicializarUI() {
        setTitle("Sistema de Gerenciamento de Pacientes");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null);
        setResizable(true);

        JPanel principal = new JPanel(new BorderLayout(10, 10));
        principal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Painel superior - Formulário
        JPanel formPanel = criarPanelFormulario();
        principal.add(formPanel, BorderLayout.NORTH);

        // Painel central - Tabela
        JPanel tabelaPanel = criarPanelTabela();
        principal.add(tabelaPanel, BorderLayout.CENTER);

        // Painel inferior - Status
        JPanel statusPanel = criarPanelStatus();
        principal.add(statusPanel, BorderLayout.SOUTH);

        add(principal);
        atualizarTabela();
    }

    private JPanel criarPanelFormulario() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Cadastro de Paciente"));

        // Campos
        JPanel camposPanel = new JPanel(new GridLayout(2, 4, 5, 5));

        camposPanel.add(new JLabel("Nome:"));
        textFieldNome = new JTextField();
        camposPanel.add(textFieldNome);

        camposPanel.add(new JLabel("CPF (XXX.XXX.XXX-XX):"));
        textFieldCPF = new JFormattedTextField(criarMascaraCPF());
        textFieldCPF.setText("");
        camposPanel.add(textFieldCPF);

        camposPanel.add(new JLabel("Data (DD/MM/YYYY):"));
        textFieldData = new JFormattedTextField(criarMascaraData());
        textFieldData.setText("");
        camposPanel.add(textFieldData);

        camposPanel.add(new JLabel("Telefone (XX) XXXXX-XXXX:"));
        textFieldTelefone = new JFormattedTextField(criarMascaraTelefone());
        textFieldTelefone.setText("");
        camposPanel.add(textFieldTelefone);

        panel.add(camposPanel, BorderLayout.NORTH);

        // Botões
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));

        JButton btnCadastrar = new JButton("Cadastrar");
        btnCadastrar.addActionListener(e -> cadastrarPaciente());
        botoesPanel.add(btnCadastrar);

        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.addActionListener(e -> atualizarPaciente());
        botoesPanel.add(btnAtualizar);

        JButton btnExcluir = new JButton("Excluir");
        btnExcluir.setForeground(Color.RED);
        btnExcluir.addActionListener(e -> excluirPaciente());
        botoesPanel.add(btnExcluir);

        JButton btnLimpar = new JButton("Limpar");
        btnLimpar.addActionListener(e -> limparFormulario());
        botoesPanel.add(btnLimpar);

        // Busca
        JPanel buscaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        buscaPanel.add(new JLabel("Buscar por nome:"));
        textFieldBusca = new JTextField(20);
        buscaPanel.add(textFieldBusca);

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(e -> buscarPacientes());
        buscaPanel.add(btnBuscar);

        JButton btnListarTodos = new JButton("Listar Todos");
        btnListarTodos.addActionListener(e -> atualizarTabela());
        buscaPanel.add(btnListarTodos);

        JPanel acoesPanelContainer = new JPanel(new GridLayout(2, 1));
        acoesPanelContainer.add(botoesPanel);
        acoesPanelContainer.add(buscaPanel);

        panel.add(acoesPanelContainer, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel criarPanelTabela() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Lista de Pacientes"));

        tableModel = new DefaultTableModel(
                new String[]{"ID", "Nome", "CPF", "Data Nascimento", "Telefone"}, 0
        );
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                try {
                    Object idObj = tableModel.getValueAt(row, 0);
                    Long id = (idObj instanceof Long) ? (Long) idObj : Long.parseLong(idObj.toString());
                    preencherFormulario(dao.buscarPacientePorId(id));
                } catch (Exception ex) {
                    // Ignorar erros de seleção
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel criarPanelStatus() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createEtchedBorder());

        labelMensagem = new JLabel("Pronto");
        panel.add(labelMensagem);

        return panel;
    }

    private void cadastrarPaciente() {
        try {
            if (textFieldNome.getText().isEmpty() || textFieldCPF.getText().isEmpty() ||
                    textFieldData.getText().isEmpty() || textFieldTelefone.getText().isEmpty()) {
                mostrarMensagem("Preencha todos os campos!", Color.RED);
                return;
            }

            Paciente paciente = new Paciente();
            paciente.setNome(textFieldNome.getText());
            paciente.setCpf(textFieldCPF.getText());
            
            // Converter data de DD/MM/YYYY para YYYY-MM-DD
            String dataStr = textFieldData.getText().replaceAll("[^0-9]", "");
            if (dataStr.length() == 8) {
                String dia = dataStr.substring(0, 2);
                String mes = dataStr.substring(2, 4);
                String ano = dataStr.substring(4, 8);
                LocalDate data = LocalDate.parse(ano + "-" + mes + "-" + dia);
                paciente.setDataNascimento(data);
            }
            
            paciente.setTelefone(textFieldTelefone.getText());

            dao.salvarPaciente(paciente);
            mostrarMensagem("Paciente cadastrado com sucesso!", Color.GREEN);
            limparFormulario();
            atualizarTabela();
        } catch (Exception e) {
            mostrarMensagem("Erro: " + e.getMessage(), Color.RED);
        }
    }

    private void atualizarPaciente() {
        try {
            int row = table.getSelectedRow();
            if (row < 0) {
                mostrarMensagem("Selecione um paciente na tabela!", Color.RED);
                return;
            }

            Object idObj = tableModel.getValueAt(row, 0);
            Long id = (idObj instanceof Long) ? (Long) idObj : Long.parseLong(idObj.toString());
            Paciente paciente = dao.buscarPacientePorId(id);

            if (paciente == null) {
                mostrarMensagem("Paciente não encontrado!", Color.RED);
                return;
            }

            if (textFieldNome.getText().isEmpty() || textFieldCPF.getText().isEmpty() ||
                    textFieldData.getText().isEmpty() || textFieldTelefone.getText().isEmpty()) {
                mostrarMensagem("Preencha todos os campos!", Color.RED);
                return;
            }

            paciente.setNome(textFieldNome.getText());
            paciente.setCpf(textFieldCPF.getText());
            
            // Converter data de DD/MM/YYYY para YYYY-MM-DD
            String dataStr = textFieldData.getText().replaceAll("[^0-9]", "");
            if (dataStr.length() == 8) {
                String dia = dataStr.substring(0, 2);
                String mes = dataStr.substring(2, 4);
                String ano = dataStr.substring(4, 8);
                LocalDate data = LocalDate.parse(ano + "-" + mes + "-" + dia);
                paciente.setDataNascimento(data);
            }
            
            paciente.setTelefone(textFieldTelefone.getText());

            dao.atualizarPaciente(paciente);
            mostrarMensagem("Paciente atualizado com sucesso!", Color.GREEN);
            limparFormulario();
            atualizarTabela();
        } catch (Exception e) {
            mostrarMensagem("Erro: " + e.getMessage(), Color.RED);
        }
    }

    private void excluirPaciente() {
        try {
            int row = table.getSelectedRow();
            if (row < 0) {
                mostrarMensagem("Selecione um paciente!", Color.RED);
                return;
            }

            Object idObj = tableModel.getValueAt(row, 0);
            Long id = (idObj instanceof Long) ? (Long) idObj : Long.parseLong(idObj.toString());
            Paciente paciente = dao.buscarPacientePorId(id);

            if (paciente != null) {
                int resultado = JOptionPane.showConfirmDialog(this,
                        "Tem certeza que deseja excluir " + paciente.getNome() + "?",
                        "Confirmar exclusão", JOptionPane.YES_NO_OPTION);

                if (resultado == JOptionPane.YES_OPTION) {
                    dao.excluirPaciente(id);
                    mostrarMensagem("Paciente excluído com sucesso!", Color.GREEN);
                    limparFormulario();
                    atualizarTabela();
                }
            }
        } catch (Exception e) {
            mostrarMensagem("Erro: " + e.getMessage(), Color.RED);
        }
    }

    private void buscarPacientes() {
        try {
            String termo = textFieldBusca.getText();
            if (termo.isEmpty()) {
                atualizarTabela();
                return;
            }

            List<Paciente> pacientes = dao.buscarPacientePorNome(termo);
            tableModel.setRowCount(0);
            for (Paciente p : pacientes) {
                tableModel.addRow(new Object[]{p.getId(), p.getNome(), p.getCpf(), p.getDataNascimento(), p.getTelefone()});
            }

            mostrarMensagem(pacientes.size() + " resultado(s)", Color.BLUE);
        } catch (Exception e) {
            mostrarMensagem("Erro: " + e.getMessage(), Color.RED);
        }
    }

    private void atualizarTabela() {
        try {
            List<Paciente> pacientes = dao.listarPacientes();
            tableModel.setRowCount(0);
            for (Paciente p : pacientes) {
                tableModel.addRow(new Object[]{p.getId(), p.getNome(), p.getCpf(), p.getDataNascimento(), p.getTelefone()});
            }
            mostrarMensagem(pacientes.size() + " paciente(s) no total", Color.BLACK);
        } catch (Exception e) {
            mostrarMensagem("Erro: " + e.getMessage(), Color.RED);
        }
    }

    private void preencherFormulario(Paciente paciente) {
        if (paciente != null) {
            textFieldNome.setText(paciente.getNome());
            textFieldCPF.setText(paciente.getCpf());
            
            // Converter data de YYYY-MM-DD para DD/MM/YYYY
            LocalDate data = paciente.getDataNascimento();
            String dataFormatada = String.format("%02d/%02d/%04d", 
                data.getDayOfMonth(), 
                data.getMonthValue(), 
                data.getYear());
            textFieldData.setText(dataFormatada);
            
            textFieldTelefone.setText(paciente.getTelefone());
        }
    }

    private void limparFormulario() {
        textFieldNome.setText("");
        textFieldCPF.setText("");
        textFieldData.setText("");
        textFieldTelefone.setText("");
        textFieldBusca.setText("");
        table.clearSelection();
    }

    private void mostrarMensagem(String mensagem, Color cor) {
        labelMensagem.setText(mensagem);
        labelMensagem.setForeground(cor);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PacienteUISwing frame = new PacienteUISwing();
            frame.setVisible(true);
        });
    }

    private MaskFormatter criarMascaraCPF() {
        try {
            return new MaskFormatter("###.###.###-##");
        } catch (Exception e) {
            return null;
        }
    }

    private MaskFormatter criarMascaraData() {
        try {
            return new MaskFormatter("##/##/####");
        } catch (Exception e) {
            return null;
        }
    }

    private MaskFormatter criarMascaraTelefone() {
        try {
            return new MaskFormatter("(##) #####-####");
        } catch (Exception e) {
            return null;
        }
    }
}
