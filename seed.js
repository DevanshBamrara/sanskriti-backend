const http = require('http');

const API_BASE = 'http://localhost:8080/api';

const SAREES = [
  { name: 'Crimson Banarasi Zari Saree', price: 45000, desc: 'Pure georgette Banarasi saree heavily hand-woven with real gold zari threads.', img: 'https://images.unsplash.com/photo-1583391733959-b13c7c88b0eb?q=80&w=1000' },
  { name: 'Kanjeevaram Royal Silk', price: 62000, desc: 'Authentic Kanjeevaram silk from South India with temple borders.', img: 'https://images.unsplash.com/photo-1610030469983-98e5486e9eef?q=80&w=1000' },
  { name: 'Midnight Blue Chiffon Saree', price: 18500, desc: 'Lightweight ethereal chiffon dotted with subtle Swarovski crystals.', img: 'https://images.unsplash.com/photo-1620800350493-27eb845a7c93?q=80&w=1000' },
  { name: 'Ivory Tant Handloom', price: 12000, desc: 'Crisp Bengal cotton handloom saree for elegant summer daytime events.', img: 'https://images.unsplash.com/photo-1620247657929-e58043fb915e?q=80&w=1000' }
];

const SUITS = [
  { name: 'Mughal Pearl Anarkali', price: 38000, desc: 'Floor-length regal ivory Anarkali suit featuring intricate pearl work and Mughal inspired motifs.', img: 'https://images.unsplash.com/photo-1596767554907-f317ee0c1a92?q=80&w=1000' },
  { name: 'Emerald Velvet Kurta Set', price: 22000, desc: 'Plush emerald green velvet kurta paired with straight silk pants.', img: 'https://images.unsplash.com/photo-1583391733975-aa3824ee79ce?q=80&w=1000' },
  { name: 'Rose Gold Sharara', price: 41000, desc: 'Heavily embroidered short kurti with a massive flared sharara in blush pink.', img: 'https://images.unsplash.com/photo-1616806530664-4e2079da67fc?q=80&w=1000' },
  { name: 'Classic Silk Salwar Kameez', price: 16000, desc: 'A minimalist but striking mustard yellow pure silk Punjabi suit.', img: 'https://images.unsplash.com/photo-1608248543803-ba4f8c70ae0b?q=80&w=1000' }
];

const postData = (path, data) => new Promise((resolve, reject) => {
  const payload = JSON.stringify(data);
  const options = {
    hostname: 'localhost',
    port: 8080,
    path: `/api${path}`,
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Content-Length': Buffer.byteLength(payload)
    }
  };

  const req = http.request(options, (res) => {
    let body = '';
    res.on('data', chunk => body += chunk);
    res.on('end', () => {
      try {
        resolve(JSON.parse(body));
      } catch (e) {
        reject(`Failed to parse: ${body}`);
      }
    });
  });

  req.on('error', reject);
  req.write(payload);
  req.end();
});

const seed = async () => {
  console.log('Seeding Sanskriti Database...');

  const allItems = [...SAREES.map(s => ({...s, cat: 'SAREE'})), ...SUITS.map(s => ({...s, cat: 'SUIT'}))];

  for (let item of allItems) {
    try {
      // 1. Create Product
      const prodRes = await postData('/products', {
        name: item.name,
        description: item.desc,
        price: item.price,
        category: item.cat
      });
      const productId = prodRes.data.id;
      console.log(`Created Product: ${item.name} (ID: ${productId})`);

      // 2. Add Image
      await postData('/images', {
        productId: productId,
        imageUrl: item.img,
        imageType: 'FRONT'
      });

      // 3. Add Variants
      const sizes = item.cat === 'SAREE' ? ['Unstitched'] : ['S', 'M', 'L'];
      for (let size of sizes) {
        const varRes = await postData('/variants', {
          productId: productId,
          size: size
        });
        const variantId = varRes.data.id;

        // 4. Add Initial Stock (Random amount between 5 - 15)
        await postData('/inventory/stock-in', {
          variantId: variantId,
          quantity: Math.floor(Math.random() * 10) + 5,
          reason: 'Initial luxury boutique stock'
        });
      }

      console.log(`✓ Completely configured ${item.name}`);
    } catch (err) {
      console.error(`Failed on ${item.name}:`, err);
    }
  }

  console.log('All gorgeous items have been curated into the Royal Vault!');
};

seed();
