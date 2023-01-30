FROM node:lts
WORKDIR /app
COPY index.js .
RUN npm init -y
RUN npm install --save express
RUN npm install --save cors
RUN npm install --save morgan
EXPOSE 8081
CMD ["node", "index.js"]