#!/usr/bin/env node

import { spawn } from 'child_process';
import { fileURLToPath } from 'url';
import path from 'path';
import fs from 'fs';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const { platform, arch } = process;

const yogurtBinaryName = process.platform === 'win32' ? 'yogurt.exe' : 'yogurt.kexe';

// Installed globally with npm, and executed with `yogurt`
const globalInstallRoot = path.resolve(
  __dirname,
  'node_modules', // nested node_modules folder
  '@acidify',
  `yogurt-${platform}-${arch}`,
);

// Installed locally, and executed with `npx yogurt`
const localInstallRoot = path.resolve(
  process.cwd(),
  'node_modules', // project's node_modules folder
  '@acidify',
  `yogurt-${platform}-${arch}`,
);

const localInstallRootNonCwd = path.resolve(
  __dirname, // yogurt
  '..', // @acidify
  '..', // project's node_modules folder
  '@acidify',
  `yogurt-${platform}-${arch}`,
);

const globalBinaryPath = path.join(globalInstallRoot, yogurtBinaryName);
const localBinaryPath = path.join(localInstallRoot, yogurtBinaryName);
const localBinaryPathNonCwd = path.join(localInstallRootNonCwd, yogurtBinaryName);

const getBinaryPath = () => {
  if (fs.existsSync(globalBinaryPath)) {
    return globalBinaryPath;
  }
  if (fs.existsSync(localBinaryPath)) {
    return localBinaryPath;
  }
  if (fs.existsSync(localBinaryPathNonCwd)) {
    return localBinaryPathNonCwd;
  }

  console.error(`Yogurt binary not found for ${platform}-${arch}, please ensure it is installed correctly.`);
  console.error('The script has looked for the binary in the following locations:');
  console.error(` - ${globalBinaryPath}`);
  console.error(` - ${localBinaryPath}`);
  console.error(` - ${localBinaryPathNonCwd}`);
  process.exit(1);
};

const binaryPath = getBinaryPath();

const child = spawn(binaryPath, [], {
  stdio: 'inherit',
});

child.on('error', (err) => {
  console.error(err);
  process.exit(1);
});

const forwardSignal = (signal) => {
  if (child.killed) {
    return;
  }
  try {
    child.kill(signal);
  } catch {
    /* ignore */
  }
};

['SIGINT', 'SIGTERM', 'SIGHUP'].forEach((sig) => {
  process.on(sig, () => forwardSignal(sig));
});

const childResult = await new Promise((resolve) => {
  child.on('exit', (code, signal) => {
    if (signal) {
      resolve({ type: 'signal', signal });
    } else {
      resolve({ type: 'code', exitCode: code ?? 1 });
    }
  });
});

if (childResult.type === 'signal') {
  process.kill(process.pid, childResult.signal);
} else {
  process.exit(childResult.exitCode);
}
