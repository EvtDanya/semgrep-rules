const config1 = {
  images: {
    remotePatterns: [
      // ruleid: nextjs-images-wildcard-hostname
      {
        protocol: 'https',
        hostname: '**',
        pathname: '/images/**',
      },
    ],
  },
};

const config2 = {
  images: {
    remotePatterns: [
      // ruleid: nextjs-images-wildcard-hostname
      {
        hostname: '**',
      },
    ],
  },
};

const config3 = {
  images: {
    remotePatterns: [
      // ok: Using wildcard hostname but optimization disabled (shouldnt trigger the rule)
      {
        protocol: 'https',
        hostname: '**',
        pathname: '/images/**',
      },
    ],
    unoptimized: true
  },
};

const config4 = {
  images: {
    remotePatterns: [
      // ok: Using localhost but port and path specified (shouldnt trigger the rule)
      {
        protocol: 'http',
        hostname: 'localhost',
        port: '8080',
        pathname: '/images/**',
      },
    ],
  },
};

const config5 = {
  images: {
    remotePatterns: [
      // ok: Specific domain (shouldnt trigger the rule)
      {
        protocol: 'https',
        hostname: 'example.com',
        port: '',
        pathname: '/images/**',
      },
    ],
  },
};

const config6 = {
  images: {
    remotePatterns: [
      // ok: Multiple specific domains (shouldnt trigger the rule)
      {
        protocol: 'https',
        hostname: 'images.example.com',
        port: '',
        pathname: '/images/**',
      },
      // ok: Multiple specific domains (shouldnt trigger the rule)
      {
        protocol: 'https',
        hostname: 'media.example.com',
        port: '',
        pathname: '/media/**',
      },
    ],
  },
};

const config7 = {
  images: {
    remotePatterns: [
      // ok: Using subdomain wildcard (shouldnt trigger the rule)
      {
        protocol: 'https',
        hostname: '*.example.com',
        port: '',
        pathname: '/images/**',
      },
    ],
  },
};

const config8 = {
  images: {
    // ruleid: nextjs-images-wildcard-hostname
    remotePatterns: [new URL('https://**/images/**')],
  },
};

const config9 = {
  images: {
    remotePatterns: [
      // ruleid: nextjs-images-localhost-hostname
      {
        hostname: 'localhost',
      },
    ],
  },
};

const config10 = {
  images: {
    // ruleid: nextjs-images-localhost-hostname
    remotePatterns: [new URL('http://localhost')],
  },
};

const config11 = {
  images: {
    // ruleid: nextjs-images-localhost-hostname
    remotePatterns: [new URL('http://127.0.0.1')],
  },
};
