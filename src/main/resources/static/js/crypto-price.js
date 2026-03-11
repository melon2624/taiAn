// API 基础 URL
const API_BASE_URL = '/restapi/cai';

// 币种列表
const CRYPTOCURRENCIES = ['BTC', 'ETH', 'TRX', 'BNB'];

// 图片列表 (会轮流切换)
const IMAGES = ['/img/1.png', '/img/2.png'];

// 当前显示的图片索引
let currentImageIndex = 0;

/**
 * 获取所有加密货币价格
 */
async function fetchPrices() {
    try {
        const priceList = document.getElementById('priceList');
        
        // 顯示加載狀態
        priceList.innerHTML = '<div class="loading"><span class="spinner"></span><span>正在加载价格数据...</span></div>';
        
        // 获取所有价格
        const pricesData = await Promise.all(
            CRYPTOCURRENCIES.map(symbol => fetchSinglePrice(symbol))
        );

        // 渲染价格列表
        renderPriceList(pricesData);
        
        // 更新时间
        updateTimeDisplay();
    } catch (error) {
        console.error('獲取價格失敗:', error);
        document.getElementById('priceList').innerHTML = 
            '<div class="error">獲取價格失敗，請檢查網絡連接後重試</div>';
    }
}

/**
 * 获取单个币种的价格
 */
async function fetchSinglePrice(symbol) {
    try {
        const response = await fetch(`${API_BASE_URL}/cryptoPrice/${symbol}`);
        
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        
        const data = await response.json();
        return {
            symbol: symbol,
            askPrice: data.askPrice || '0',
            bidPrice: data.bidPrice || '0',
            lastUpdateTime: data.lastUpdateTime || Date.now()
        };
    } catch (error) {
        console.error(`獲取 ${symbol} 價格失敗:`, error);
        return {
            symbol: symbol,
            askPrice: '0',
            bidPrice: '0',
            lastUpdateTime: Date.now()
        };
    }
}

/**
 * 渲染价格列表
 */
function renderPriceList(pricesData) {
    const priceList = document.getElementById('priceList');
    
    if (!pricesData || pricesData.length === 0) {
        priceList.innerHTML = '<div class="error">沒有價格數據</div>';
        return;
    }

    priceList.innerHTML = pricesData.map(price => {
        // 格式化價格
        const sellPrice = formatPrice(price.askPrice);
        const buyPrice = formatPrice(price.bidPrice);

        return `
            <div class="price-item">
                <div class="symbol">${price.symbol}</div>
                <div class="sell-price">${sellPrice}</div>
                <div class="buy-price">${buyPrice}</div>
            </div>
        `;
    }).join('');
}

/**
 * 格式化價格顯示
 */
function formatPrice(price) {
    if (!price || price === '0') {
        return '0.00';
    }

    const num = parseFloat(price);
    
    if (isNaN(num)) {
        return '0.00';
    }

    // 根據價格大小決定小數位數
    if (num >= 100) {
        return num.toLocaleString('zh-CN', {
            minimumFractionDigits: 2,
            maximumFractionDigits: 2
        });
    } else if (num >= 1) {
        return num.toLocaleString('zh-CN', {
            minimumFractionDigits: 2,
            maximumFractionDigits: 4
        });
    } else {
        return num.toLocaleString('zh-CN', {
            minimumFractionDigits: 4,
            maximumFractionDigits: 8
        });
    }
}

/**
 * 更新顯示時間
 */
function updateTimeDisplay() {
    const now = new Date();
    const timeString = now.toLocaleString('zh-HK', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit',
        hour12: false
    });
    
    document.getElementById('updateTime').textContent = `最後更新時間: ${timeString}`;
}

/**
 * 初始化圖片輪播
 */
function initImageCarousel() {
    const carousel = document.getElementById('imageCarousel');
    
    // 清空佔位符
    carousel.innerHTML = '';
    
    // 創建圖片元素
    IMAGES.forEach((imgPath, index) => {
        const img = document.createElement('img');
        img.src = imgPath;
        img.alt = `Image ${index + 1}`;
        img.className = 'carousel-image';
        carousel.appendChild(img);
    });
    
    // 啟動輪播
    startImageCarousel();
}

/**
 * 啟動圖片輪播
 */
function startImageCarousel() {
    const images = document.querySelectorAll('.carousel-image');
    
    if (images.length === 0) return;
    
    setInterval(() => {
        // 移除所有 active 類
        images.forEach(img => img.classList.remove('active'));
        
        // 添加當前圖片的 active 類，觸發動畫
        images[currentImageIndex].classList.add('active');
        
        // 更新索引
        currentImageIndex = (currentImageIndex + 1) % images.length;
    }, 6000); // 6秒切換一次
}

/**
 * 頁面加載時初始化
 */
document.addEventListener('DOMContentLoaded', function() {
    fetchPrices();
    initImageCarousel();
    
    // 每10分鐘自動刷新一次價格
    setInterval(fetchPrices, 600000);
});

/**
/**
 * 每秒刷新顯示時間
 */
setInterval(updateTimeDisplay, 1000);

/**
 * 打開二維碼彈窗
 */
function openQrModal(imageSrc, title, description) {
    document.getElementById('qrModalImg').src = imageSrc;
    document.getElementById('qrModalImg').alt = title;
    document.getElementById('qrModalTitle').textContent = title;
    document.getElementById('qrModalDesc').textContent = description;
    document.getElementById('qrModalOverlay').classList.add('active');
}

/**
 * 關閉二維碼彈窗
 */
function closeQrModal() {
    document.getElementById('qrModalOverlay').classList.remove('active');
}

// 按 ESC 鍵關閉彈窗
document.addEventListener('keydown', function(e) {
    if (e.key === 'Escape') closeQrModal();
});
