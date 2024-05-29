# Use a imagem oficial do Nginx
FROM nginx:alpine

# Remova a configuração padrão do Nginx
RUN rm /etc/nginx/conf.d/default.conf

# Copie a nova configuração do Nginx
COPY nginx.conf /etc/nginx/conf.d

# Copie os arquivos HTML para o diretório padrão do Nginx
COPY html /usr/share/nginx/html
COPY css /usr/share/nginx/html/css
COPY js /usr/share/nginx/html/js

# Exponha a porta 80
EXPOSE 80
