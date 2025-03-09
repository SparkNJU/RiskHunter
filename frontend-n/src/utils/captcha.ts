class CaptchaGenerator {
    private readonly width: number = 120;
    private readonly height: number = 40;
    private readonly captchaLength: number = 4;
    private code: string = '';
    
    // 生成随机颜色
    private randomColor(): string {
        const r = Math.floor(Math.random() * 256);
        const g = Math.floor(Math.random() * 256);
        const b = Math.floor(Math.random() * 256);
        return `rgb(${r},${g},${b})`;
    }
    
    // 生成随机字符
    private randomChar(): string {
        const chars = '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';
        return chars.charAt(Math.floor(Math.random() * chars.length));
    }
    
    // 生成验证码
    generate(): { image: string; code: string } {
        const canvas = document.createElement('canvas');
        canvas.width = this.width;
        canvas.height = this.height;
        const ctx = canvas.getContext('2d')!;
        
        // 填充白色背景
        ctx.fillStyle = '#fff';
        ctx.fillRect(0, 0, this.width, this.height);
        
        // 生成验证码文本
        this.code = '';
        for (let i = 0; i < this.captchaLength; i++) {
            const char = this.randomChar();
            this.code += char;
            ctx.fillStyle = this.randomColor();
            ctx.font = '26px Arial';
            const x = (i + 1) * (this.width / (this.captchaLength + 1));
            const y = this.height / 2 + 8;
            const rotate = (Math.random() - 0.5) * 0.3;
            ctx.translate(x, y);
            ctx.rotate(rotate);
            ctx.fillText(char, -10, 0);
            ctx.rotate(-rotate);
            ctx.translate(-x, -y);
        }
        
        // 添加干扰线
        for (let i = 0; i < 3; i++) {
            ctx.strokeStyle = this.randomColor();
            ctx.beginPath();
            ctx.moveTo(Math.random() * this.width, Math.random() * this.height);
            ctx.lineTo(Math.random() * this.width, Math.random() * this.height);
            ctx.stroke();
        }
        
        // 添加干扰点
        for (let i = 0; i < 30; i++) {
            ctx.fillStyle = this.randomColor();
            ctx.beginPath();
            ctx.arc(Math.random() * this.width, Math.random() * this.height, 1, 0, 2 * Math.PI);
            ctx.fill();
        }
        
        return {
            image: canvas.toDataURL('image/png'),
            code: this.code
        };
    }
    
    // 验证输入是否正确
    validate(input: string): boolean {
        return input.toLowerCase() === this.code.toLowerCase();
    }
}

export const captchaGenerator = new CaptchaGenerator();