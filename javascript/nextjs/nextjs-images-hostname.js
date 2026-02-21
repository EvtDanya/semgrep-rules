// rule-id: nextjs-images-wildcard-hostname
const config1 = {
  images: {
    remotePatterns: [
      {
        protocol: 'https',
        hostname: '**',
        pathname: '/images/**',
      },
    ],
  },
};

// rule-id: nextjs-images-wildcard-hostname
const config2 = {
  images: {
    remotePatterns: [
      {
        hostname: '**',
      },
    ],
  },
};

// OK: Using wildcard hostname but optimization disabled (shouldnt trigger the rule)
const config3 = {
  images: {
    remotePatterns: [
      {
        protocol: 'https',
        hostname: '**',
        pathname: '/images/**',
      },
    ],
    unoptimized: true
  },
};

// OK: Using wildcard hostname but port and path specified (shouldnt trigger the rule)
const config4 = {
  images: {
    remotePatterns: [
      {
        protocol: 'https',
        hostname: '**',
        port: '8080',
        pathname: '/images/**',
      },
    ],
  },
};

// OK: Specific domain (shouldnt trigger the rule)
const config5 = {
  images: {
    remotePatterns: [
      {
        protocol: 'https',
        hostname: 'example.com',
        port: '',
        pathname: '/images/**',
      },
    ],
  },
};

// OK: Multiple specific domains (shouldnt trigger the rule)
const config6 = {
  images: {
    remotePatterns: [
      {
        protocol: 'https',
        hostname: 'images.example.com',
        port: '',
        pathname: '/images/**',
      },
      {
        protocol: 'https',
        hostname: 'media.example.com',
        port: '',
        pathname: '/media/**',
      },
    ],
  },
};

// OK: Using subdomain wildcard (shouldnt trigger the rule)
const config7 = {
  images: {
    remotePatterns: [
      {
        protocol: 'https',
        hostname: '*.example.com',
        port: '',
        pathname: '/images/**',
      },
    ],
  },
};

// rule-id: nextjs-images-wildcard-hostname
const config8 = {
  images: {
    remotePatterns: [new URL('https://**/images/**')],
  },
};

// rule-id: nextjs-images-localhost-hostname
const config9 = {
  images: {
    remotePatterns: [
      {
        hostname: 'localhost',
      },
    ],
  },
};

// rule-id: nextjs-images-localhost-hostname
const config10 = {
  images: {
    remotePatterns: [new URL('http://localhost')],
  },
};

// rule-id: nextjs-images-localhost-hostname
const config11 = {
  images: {
    remotePatterns: [new URL('http://127.0.0.1')],
  },
};
