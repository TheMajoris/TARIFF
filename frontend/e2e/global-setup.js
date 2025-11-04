import { execSync } from 'child_process';
import { fileURLToPath } from 'url';
import path from 'path';

export default async () => {
    console.log('🚀 Starting backend Docker container...');
    try {
        // Example using docker-compose
        const __filename = fileURLToPath(import.meta.url);
        const __dirname = path.dirname(__filename);
        const coreDir = path.resolve(__dirname, '../../core');
        execSync('docker-compose up --build -d', {
            cwd: coreDir,        // run inside /core directory
            stdio: 'inherit',    // print Docker logs directly
        });

        const BACKEND_URL = "http://localhost:8080"
        const MAX_ATTEMPTS = 100;
        const DELAY_MS = 1000; // 1 second pause between attempts

        for (let i = 1; i <= MAX_ATTEMPTS; i++) {
            try {
                const res = await fetch(BACKEND_URL);
                if (res.status === 200) {
                    console.log('✅ Backend is ready!');
                    return;
                }
            } catch (error) {
                // Backend not responding yet
                console.log('⏳ Waiting for backend to be ready...');
            }

            // ⏸ Wait 1s before trying again
            await new Promise(r => setTimeout(r, DELAY_MS));
        }

    } catch (err) {
        console.error('❌ Failed to start Docker container', err);
        process.exit(1);
    }
};