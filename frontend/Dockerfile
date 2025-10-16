# ---- Build Stage ----
# Use an official Node.js image with a version that meets your requirements
FROM node:22-alpine AS builder

# Set the working directory
WORKDIR /app

# Copy package files AND your Svelte config file
COPY package.json package-lock.json svelte.config.js ./

# Install ALL dependencies, but do not run any pre/post install scripts yet
RUN NODE_ENV=development npm ci --ignore-scripts

# Copy the rest of the application code
COPY . .

# ---- THIS IS THE KEY CHANGE ----
# Now that all packages are installed, manually run the svelte-kit sync command
RUN npm run prepare

# Build the application
# This will now succeed because the sync command has run and all packages are present
RUN npm run build

# ---- Production Stage ----
# Use a lightweight web server to serve the static files
FROM nginx:stable-alpine

# Copy the built files from the "builder" stage
# Make sure this points to the correct output directory ('build')
COPY --from=builder /app/build /usr/share/nginx/html

# Expose port 80
EXPOSE 80

# The default Nginx command will start the server
CMD ["nginx", "-g", "daemon off;"]
