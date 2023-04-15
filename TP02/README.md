# TP02

Indexação: Árvore B, Hashing estendido e Lista invertida

- Link do vídeo: <>
- Observações sobre implementação:
    - Create: IDs dos registros são setados na criação, somando um ao valor do último ID, armazenado no arquivo de registros; retorna se foi possível adicionar chave nos arquivos de índice corretamente (cria chave em todos índices em toda criação de registro).
    - Read: permite a escolha de qual índice deve ser usado para pesquisar chave (Árvore B ou Hashing).
    - Update: também permite a escolha de qual índice deve ser usado para pesquisar chave (Árvore B ou Hashing); faz uma leitura usando função Read primeiro, para mostrar registro atual, depois lê atualização desejada; se tamanho do registro não mudou, mantêm índices, se não faz um Delete depois um Create (tanto no arquivo de registros quanto nos de índice), alterando ID do novo registro; retorna se foi possível atualizar chave nos arquivos de índice corretamente.
    - Delete: também permite a escolha de qual índice deve ser usado para pesquisar chave (Árvore B ou Hashing); faz remoção lógica do arquivo de dados (lápide) e deleta chave dos arquivos de índice e retorna se foi possível deletar corretamente.
    - Árvore B: indexação usando campo ID; escolha de implementar a B primeiro por ser mais fácil; devido à dificuldade de se implementar a inserção e remoção de chaves diretamente na memória secundária, elas são feitas em memória primária, depois o arquivo é atualizado.
    - Hashing Estendido: indexação usando campo ID; menu interno imprime diretório e buckets do hash (somente posições preenchidas); remoção de chaves não reduz profundidades.
    - Listas invertidas: dois arquivos, para as duas listas, uma de nomes e uma de artistas das músicas; (...) 
