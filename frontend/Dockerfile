FROM node:26

WORKDIR /app

COPY package*.json ./
RUN npm install

CMD ["npm", "run", "dev", "--", "--host"]
