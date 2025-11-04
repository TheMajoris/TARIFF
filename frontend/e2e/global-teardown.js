import { execSync } from 'child_process';
import { fileURLToPath } from 'url';
import path from 'path';

export default async () => {
    console.log('🧹 Stopping backend Docker container...');
    try {
        const __filename = fileURLToPath(import.meta.url);
        const __dirname = path.dirname(__filename);
        const coreDir = path.resolve(__dirname, '../../core');
        execSync('docker compose down -v', {
            cwd: coreDir,        // run inside /core directory
            stdio: 'inherit',    // print Docker logs directly
        });

    } catch (err) {
        console.error('❌ Failed to stop Docker container', err);
    }
};