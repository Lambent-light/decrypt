import CryptoJS from 'crypto-js';

const KEY = CryptoJS.enc.Utf8.parse('abcdefghijklmnop');
const IV = CryptoJS.enc.Utf8.parse('abcdefghijklmnop');

/**
 * 核心解密逻辑
 * @param data 加密后的字符串（可能包含 "data": 前缀和逗号）
 * @returns 解密后的明文
 */
export function decryptData(data: string): string {
    try {
        let cleanData = data.trim();

        // 更加鲁棒的预处理逻辑，处理各种粘贴格式
        // 例如: 
        // 1. "data": "base64..."
        // 2. data : "base64..."
        // 3. data \n : \n "base64..."

        // 如果包含 "data" 和 ":"，尝试提取冒号后的内容
        if (/(?:"data"|data)\s*[:：]/i.test(cleanData)) {
            const parts = cleanData.split(/[:：]/);
            if (parts.length > 1) {
                // 取冒号之后的所有部分并拼接（以防加密串里也有冒号，虽然AES Base64里没有，但为了安全）
                cleanData = parts.slice(1).join(':').trim();
            }
        }

        // 去掉首尾的引号、逗号等干扰字符
        cleanData = cleanData.replace(/^["']|["']$/g, ''); // 去首尾引号
        cleanData = cleanData.replace(/,$/, '');           // 去结尾逗号
        cleanData = cleanData.replace(/\s/g, '');          // 去除所有空格/换行


        // 执行解密
        const decrypted = CryptoJS.AES.decrypt(cleanData, KEY, {
            iv: IV,
            mode: CryptoJS.mode.CBC,
            padding: CryptoJS.pad.Pkcs7, // CryptoJS.pad.Pkcs7 兼容 Java 的 PKCS5Padding
        });

        const result = decrypted.toString(CryptoJS.enc.Utf8).trim();

        // 如果解密失败导致结果为空字符串，可能是原始输入无效或不支持，返回原文本（类似 Java 报错捕获后的逻辑）
        return result || data;
    } catch (error) {
        console.error('Decryption failed:', error);
        return data;
    }
}
