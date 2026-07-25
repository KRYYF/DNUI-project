<template>
  <div class="login-page">
    <!-- ==================== 左侧：环保插画面板 ==================== -->
    <div class="illustration-panel">
      <!-- 装饰光晕 -->
      <div class="glow glow-1"></div>
      <div class="glow glow-2"></div>
      <div class="glow glow-3"></div>

      <!-- SVG 插画 -->
      <div class="illustration-wrapper">
        <svg viewBox="0 0 600 620" fill="none" xmlns="http://www.w3.org/2000/svg" class="illustration-svg">
          <!-- ===== 渐变定义 ===== -->
          <defs>
            <!-- 天空 -->
            <linearGradient id="skyGrad" x1="0" y1="0" x2="0" y2="1">
              <stop offset="0%" stop-color="#74B9FF"/>
              <stop offset="40%" stop-color="#A8D8FF"/>
              <stop offset="75%" stop-color="#D4ECFF"/>
              <stop offset="100%" stop-color="#E8F4E8"/>
            </linearGradient>

            <!-- 太阳 -->
            <radialGradient id="sunGlow" cx="0.5" cy="0.5" r="0.5">
              <stop offset="0%" stop-color="#FFF9C4"/>
              <stop offset="40%" stop-color="#FFE082"/>
              <stop offset="100%" stop-color="#FFB74D" stop-opacity="0"/>
            </radialGradient>

            <!-- 远山 (带雪顶) -->
            <linearGradient id="farMountain" x1="0" y1="0" x2="0" y2="1">
              <stop offset="0%" stop-color="#E8E8F0"/>
              <stop offset="12%" stop-color="#D0D8E8"/>
              <stop offset="28%" stop-color="#8BAAC4"/>
              <stop offset="55%" stop-color="#5D8CA8"/>
              <stop offset="100%" stop-color="#3D6E8A"/>
            </linearGradient>

            <!-- 中山 (更绿) -->
            <linearGradient id="midMountain" x1="0" y1="0" x2="0" y2="1">
              <stop offset="0%" stop-color="#C8DCC0"/>
              <stop offset="20%" stop-color="#7BAF7A"/>
              <stop offset="55%" stop-color="#4D8F4D"/>
              <stop offset="100%" stop-color="#2D6A2D"/>
            </linearGradient>

            <!-- 近山 -->
            <linearGradient id="nearMountain" x1="0" y1="0" x2="0" y2="1">
              <stop offset="0%" stop-color="#A8D8A0"/>
              <stop offset="30%" stop-color="#5DA05D"/>
              <stop offset="70%" stop-color="#3D7A3D"/>
              <stop offset="100%" stop-color="#245A28"/>
            </linearGradient>

            <!-- 湖泊 -->
            <linearGradient id="lakeGrad" x1="0" y1="0" x2="0" y2="1">
              <stop offset="0%" stop-color="#5BA0C8"/>
              <stop offset="30%" stop-color="#4A90B8"/>
              <stop offset="70%" stop-color="#3578A0"/>
              <stop offset="100%" stop-color="#286088"/>
            </linearGradient>

            <!-- 湖水反光 -->
            <linearGradient id="lakeShine" x1="0" y1="0" x2="1" y2="0">
              <stop offset="0%" stop-color="#fff" stop-opacity="0"/>
              <stop offset="40%" stop-color="#fff" stop-opacity="0.15"/>
              <stop offset="60%" stop-color="#fff" stop-opacity="0.25"/>
              <stop offset="100%" stop-color="#fff" stop-opacity="0"/>
            </linearGradient>

            <!-- 树木渐变 -->
            <linearGradient id="pineGrad1" x1="0" y1="0" x2="0" y2="1">
              <stop offset="0%" stop-color="#5CB870"/>
              <stop offset="100%" stop-color="#2D6A38"/>
            </linearGradient>
            <linearGradient id="pineGrad2" x1="0" y1="0" x2="0" y2="1">
              <stop offset="0%" stop-color="#48A058"/>
              <stop offset="100%" stop-color="#1E5A28"/>
            </linearGradient>
            <linearGradient id="pineGrad3" x1="0" y1="0" x2="0" y2="1">
              <stop offset="0%" stop-color="#6ED080"/>
              <stop offset="100%" stop-color="#358040"/>
            </linearGradient>

            <!-- 太阳能板面板渐变 -->
            <linearGradient id="panelGrad" x1="0" y1="0" x2="1" y2="1">
              <stop offset="0%" stop-color="#1A237E"/>
              <stop offset="50%" stop-color="#283593"/>
              <stop offset="100%" stop-color="#1A237E"/>
            </linearGradient>
            <linearGradient id="panelGrad2" x1="0" y1="0" x2="1" y2="1">
              <stop offset="0%" stop-color="#0D1B5E"/>
              <stop offset="50%" stop-color="#1A2D8A"/>
              <stop offset="100%" stop-color="#0D1B5E"/>
            </linearGradient>
            <linearGradient id="panelShine" x1="0" y1="0" x2="0.4" y2="0.6">
              <stop offset="0%" stop-color="#fff" stop-opacity="0.2"/>
              <stop offset="100%" stop-color="#fff" stop-opacity="0"/>
            </linearGradient>

            <!-- 云朵 -->
            <radialGradient id="cloudGrad" cx="0.5" cy="0.5" r="0.5">
              <stop offset="0%" stop-color="#FFFFFF"/>
              <stop offset="70%" stop-color="#F0F6FC"/>
              <stop offset="100%" stop-color="#E0ECF5"/>
            </radialGradient>

            <!-- 树叶渐变 -->
            <linearGradient id="leafGrad" x1="0" y1="0" x2="1" y2="1">
              <stop offset="0%" stop-color="#7ED89A"/>
              <stop offset="100%" stop-color="#45A858"/>
            </linearGradient>

            <!-- 山体纹理滤镜 -->
            <filter id="mountainTexture">
              <feTurbulence type="fractalNoise" baseFrequency="0.04" numOctaves="3" seed="2"/>
              <feColorMatrix type="saturate" values="0"/>
              <feBlend in="SourceGraphic" mode="multiply" result="textured"/>
            </filter>
          </defs>

          <!-- ===== 天空 ===== -->
          <rect x="0" y="0" width="600" height="620" fill="url(#skyGrad)"/>

          <!-- ===== 太阳 ===== -->
          <circle cx="470" cy="130" r="80" fill="url(#sunGlow)" opacity="0.6">
            <animate attributeName="r" values="80;88;80" dur="5s" repeatCount="indefinite"/>
          </circle>
          <circle cx="470" cy="130" r="38" fill="#FFF9C4" opacity="0.95">
            <animate attributeName="r" values="38;42;38" dur="5s" repeatCount="indefinite"/>
          </circle>
          <circle cx="470" cy="130" r="34" fill="#FFE082">
            <animate attributeName="r" values="34;37;34" dur="5s" repeatCount="indefinite"/>
          </circle>

          <!-- ===== 云朵1 (左侧大云) ===== -->
          <g opacity="0.85">
            <animateTransform attributeName="transform" type="translate" values="0,0;18,0;0,0" dur="10s" repeatCount="indefinite"/>
            <ellipse cx="100" cy="100" rx="60" ry="24" fill="url(#cloudGrad)"/>
            <ellipse cx="135" cy="88" rx="44" ry="28" fill="url(#cloudGrad)"/>
            <ellipse cx="70" cy="90" rx="38" ry="20" fill="url(#cloudGrad)"/>
            <ellipse cx="110" cy="80" rx="32" ry="18" fill="#FFFFFF"/>
          </g>

          <!-- ===== 云朵2 (右上小云) ===== -->
          <g opacity="0.65">
            <animateTransform attributeName="transform" type="translate" values="0,0;-12,0;0,0" dur="13s" repeatCount="indefinite"/>
            <ellipse cx="330" cy="72" rx="48" ry="18" fill="url(#cloudGrad)"/>
            <ellipse cx="355" cy="62" rx="34" ry="22" fill="url(#cloudGrad)"/>
            <ellipse cx="305" cy="65" rx="30" ry="16" fill="url(#cloudGrad)"/>
          </g>

          <!-- ===== 飞鸟群 ===== -->
          <g opacity="0.55">
            <animateTransform attributeName="transform" type="translate" values="0,0;-35,-8;-70,-3" dur="14s" repeatCount="indefinite"/>
            <!-- 鸟1 -->
            <path d="M195 68 Q200 58 205 68" stroke="#555" stroke-width="1.8" fill="none" stroke-linecap="round"/>
            <path d="M207 68 Q212 58 217 68" stroke="#555" stroke-width="1.8" fill="none" stroke-linecap="round"/>
            <!-- 鸟2 -->
            <path d="M222 58 Q226 50 230 58" stroke="#555" stroke-width="1.5" fill="none" stroke-linecap="round"/>
            <path d="M232 58 Q236 50 240 58" stroke="#555" stroke-width="1.5" fill="none" stroke-linecap="round"/>
            <!-- 鸟3 -->
            <path d="M180 78 Q183 72 186 78" stroke="#666" stroke-width="1.2" fill="none" stroke-linecap="round"/>
            <path d="M188 78 Q191 72 194 78" stroke="#666" stroke-width="1.2" fill="none" stroke-linecap="round"/>
          </g>

          <!-- ============================================ -->
          <!-- ===== 远山层 (带雪顶): 最远景 ===== -->
          <!-- ============================================ -->
          <g>
            <!-- 远山主体 — 3座山峰 -->
            <path d="
              M-20 390
              L80 220 L160 270
              L260 195 L360 280
              L460 210 L560 270
              L620 240 L620 620 L-20 620 Z
            " fill="url(#farMountain)" opacity="0.55"/>
            <!-- 远山雪顶 -->
            <path d="
              M80 220 L92 240 L84 244 L80 220
              M260 195 L276 218 L268 222 L260 195
              M460 210 L476 234 L468 238 L460 210
            " fill="#F4F6FA" opacity="0.9"/>
            <!-- 远山阴影面 -->
            <path d="
              M260 195 L360 280 L460 210 L560 270 L620 240 L620 620 L260 620 Z
            " fill="#2E5A7A" opacity="0.18"/>
          </g>

          <!-- ============================================ -->
          <!-- ===== 中山层 (带森林覆盖纹理) ===== -->
          <!-- ============================================ -->
          <g>
            <!-- 中山主体 -->
            <path d="
              M-20 450
              L40 330 L80 355
              L130 295 L180 340
              L235 290 L285 335
              L340 280 L395 325
              L450 275 L500 320
              L555 288 L600 315
              L620 310 L620 620 L-20 620 Z
            " fill="url(#midMountain)" opacity="0.78"/>
            <!-- 中山脊线 -->
            <path d="
              M40 330 L80 355 L130 295 L180 340 L235 290 L285 335 L340 280 L395 325 L450 275 L500 320 L555 288 L600 315
            " stroke="#6AAD6A" stroke-width="1.8" fill="none" opacity="0.55"/>
            <!-- 中山暗面 -->
            <path d="
              M340 280 L395 325 L450 275 L500 320 L555 288 L600 315 L620 310 L620 620 L340 620 Z
            " fill="#1E4A1E" opacity="0.18"/>
            <!-- 山脊树影1 -->
            <g transform="translate(140, 305) scale(0.5)">
              <polygon points="0,-20 -8,0 8,0" fill="#2D5A2D" opacity="0.5"/>
              <polygon points="0,-32 -6,-10 6,-10" fill="#2D5A2D" opacity="0.4"/>
            </g>
            <g transform="translate(175, 322) scale(0.4)">
              <polygon points="0,-22 -9,0 9,0" fill="#2D5A2D" opacity="0.45"/>
              <polygon points="0,-34 -7,-12 7,-12" fill="#2D5A2D" opacity="0.35"/>
            </g>
            <g transform="translate(248, 305) scale(0.45)">
              <polygon points="0,-24 -10,0 10,0" fill="#2D5A2D" opacity="0.5"/>
              <polygon points="0,-36 -8,-12 8,-12" fill="#2D5A2D" opacity="0.4"/>
            </g>
            <g transform="translate(348, 295) scale(0.5)">
              <polygon points="0,-22 -8,0 8,0" fill="#2D5A2D" opacity="0.45"/>
              <polygon points="0,-34 -6,-11 6,-11" fill="#2D5A2D" opacity="0.35"/>
            </g>
            <g transform="translate(460, 290) scale(0.45)">
              <polygon points="0,-20 -9,0 9,0" fill="#2D5A2D" opacity="0.5"/>
              <polygon points="0,-30 -7,-10 7,-10" fill="#2D5A2D" opacity="0.4"/>
            </g>
            <g transform="translate(510, 308) scale(0.4)">
              <polygon points="0,-22 -8,0 8,0" fill="#2D5A2D" opacity="0.4"/>
              <polygon points="0,-32 -6,-11 6,-11" fill="#2D5A2D" opacity="0.3"/>
            </g>
            <g transform="translate(565, 300) scale(0.45)">
              <polygon points="0,-20 -10,0 10,0" fill="#2D5A2D" opacity="0.4"/>
              <polygon points="0,-30 -8,-10 8,-10" fill="#2D5A2D" opacity="0.3"/>
            </g>
            <!-- 中山雪痕 -->
            <path d="
              M130 295 L138 308 L133 310 L130 295
              M235 290 L244 305 L238 307 L235 290
              M340 280 L350 296 L344 298 L340 280
              M450 275 L460 292 L454 294 L450 275
            " fill="#DCE8DC" opacity="0.6"/>
          </g>

          <!-- ============================================ -->
          <!-- ===== 近山层 (带草地纹理和树影) ===== -->
          <!-- ============================================ -->
          <g>
            <!-- 近山主体 -->
            <path d="
              M-20 500
              L70 375 L130 410
              L195 360 L250 400
              L320 350 L380 395
              L445 355 L500 405
              L560 365 L620 380
              L620 620 L-20 620 Z
            " fill="url(#nearMountain)"/>
            <!-- 近山暗面 -->
            <path d="
              M380 395 L445 355 L500 405 L560 365 L620 380 L620 620 L380 620 Z
            " fill="#1A3A1A" opacity="0.2"/>
            <!-- 近山主脊线 -->
            <path d="
              M70 375 L130 410 L195 360 L250 400 L320 350 L380 395 L445 355 L500 405 L560 365 L620 380
            " stroke="#3D7A3D" stroke-width="2" fill="none" opacity="0.45"/>
            <!-- 近山副纹理线 (山谷) -->
            <path d="
              M100 390 L140 418 L180 390 L210 418 L270 385 L310 418 L350 385 L400 410 L440 385 L480 418 L520 390 L555 415
            " stroke="#2D6A2D" stroke-width="0.8" fill="none" opacity="0.2"/>
            <!-- 近山树影 -->
            <g transform="translate(90, 390) scale(0.6)">
              <polygon points="0,-22 -10,0 10,0" fill="#1E4A1E" opacity="0.5"/>
              <polygon points="0,-36 -8,-10 8,-10" fill="#1E4A1E" opacity="0.4"/>
            </g>
            <g transform="translate(155, 385) scale(0.55)">
              <polygon points="0,-20 -9,0 9,0" fill="#1E4A1E" opacity="0.4"/>
              <polygon points="0,-32 -7,-9 7,-9" fill="#1E4A1E" opacity="0.35"/>
            </g>
            <g transform="translate(208, 375) scale(0.65)">
              <polygon points="0,-24 -11,0 11,0" fill="#1E4A1E" opacity="0.5"/>
              <polygon points="0,-38 -9,-11 9,-11" fill="#1E4A1E" opacity="0.4"/>
            </g>
            <g transform="translate(300, 370) scale(0.55)">
              <polygon points="0,-20 -9,0 9,0" fill="#1E4A1E" opacity="0.4"/>
              <polygon points="0,-30 -7,-9 7,-9" fill="#1E4A1E" opacity="0.35"/>
            </g>
            <g transform="translate(420, 368) scale(0.6)">
              <polygon points="0,-22 -10,0 10,0" fill="#1E4A1E" opacity="0.45"/>
              <polygon points="0,-34 -8,-10 8,-10" fill="#1E4A1E" opacity="0.35"/>
            </g>
            <g transform="translate(515, 388) scale(0.5)">
              <polygon points="0,-18 -8,0 8,0" fill="#1E4A1E" opacity="0.4"/>
              <polygon points="0,-28 -6,-8 6,-8" fill="#1E4A1E" opacity="0.3"/>
            </g>
            <!-- 近山岩石点 -->
            <circle cx="220" cy="388" r="2" fill="#3D6A3D" opacity="0.3"/>
            <circle cx="350" cy="375" r="2.5" fill="#3D6A3D" opacity="0.25"/>
            <circle cx="470" cy="390" r="1.8" fill="#3D6A3D" opacity="0.3"/>
          </g>

          <!-- ============================================ -->
          <!-- ===== 湖泊 ===== -->
          <!-- ============================================ -->
          <g>
            <!-- 湖面主体 -->
            <ellipse cx="300" cy="465" rx="185" ry="50" fill="url(#lakeGrad)" opacity="0.85"/>
            <!-- 湖面反光条纹 -->
            <ellipse cx="300" cy="458" rx="150" ry="28" fill="url(#lakeShine)" opacity="0.6"/>
            <ellipse cx="290" cy="452" rx="80" ry="12" fill="#fff" opacity="0.12"/>
            <!-- 湖边浅水 -->
            <ellipse cx="300" cy="475" rx="175" ry="38" fill="#4A90B8" opacity="0.3"/>
          </g>

          <!-- ============================================ -->
          <!-- ===== 松树群 (左侧) ===== -->
          <!-- ============================================ -->
          <g>
            <!-- 松树模板：三角形叠层 -->
            <!-- 左1 - 大松树 -->
            <g transform="translate(95, 410)">
              <rect x="-4" y="0" width="8" height="45" rx="2" fill="#5D4037"/>
              <polygon points="0,-65 -32,-10 32,-10" fill="url(#pineGrad1)"/>
              <polygon points="0,-90 -27,-30 27,-30" fill="url(#pineGrad3)"/>
              <polygon points="0,-110 -22,-55 22,-55" fill="#6ED080"/>
            </g>
            <!-- 左2 - 中松树 -->
            <g transform="translate(60, 435)">
              <rect x="-3" y="0" width="6" height="32" rx="1.5" fill="#5D4037"/>
              <polygon points="0,-48 -24,-5 24,-5" fill="url(#pineGrad2)"/>
              <polygon points="0,-68 -20,-22 20,-22" fill="url(#pineGrad1)"/>
              <polygon points="0,-84 -16,-38 16,-38" fill="#48A058"/>
            </g>
            <!-- 左3 - 小松树 -->
            <g transform="translate(130, 445)">
              <rect x="-2.5" y="0" width="5" height="25" rx="1" fill="#5D4037"/>
              <polygon points="0,-40 -20,-3 20,-3" fill="url(#pineGrad1)"/>
              <polygon points="0,-58 -16,-18 16,-18" fill="#6ED080"/>
            </g>
          </g>

          <!-- ============================================ -->
          <!-- ===== 松树群 (右侧) ===== -->
          <!-- ============================================ -->
          <g>
            <g transform="translate(490, 425)">
              <rect x="-3.5" y="0" width="7" height="38" rx="1.5" fill="#5D4037"/>
              <polygon points="0,-58 -28,-8 28,-8" fill="url(#pineGrad1)"/>
              <polygon points="0,-80 -24,-25 24,-25" fill="url(#pineGrad3)"/>
              <polygon points="0,-98 -19,-48 19,-48" fill="#6ED080"/>
            </g>
            <g transform="translate(530, 448)">
              <rect x="-3" y="0" width="6" height="28" rx="1" fill="#5D4037"/>
              <polygon points="0,-44 -22,-4 22,-4" fill="url(#pineGrad2)"/>
              <polygon points="0,-62 -18,-20 18,-20" fill="#48A058"/>
            </g>
          </g>

          <!-- ============================================ -->
          <!-- ===== 太阳能板阵列 ===== -->
          <!-- ============================================ -->
          <!-- 阵列1: 前景左侧主阵列 -->
          <g transform="translate(160, 478)">
            <!-- 支架立柱 -->
            <rect x="15" y="-2" width="4" height="48" rx="1" fill="#78909C"/>
            <rect x="82" y="-2" width="4" height="48" rx="1" fill="#78909C"/>
            <!-- 横梁 -->
            <rect x="10" y="10" width="82" height="3" rx="1" fill="#90A4AE"/>
            <!-- 倾斜面板 (3块并排) -->
            <!-- 面板1 -->
            <g transform="skewX(-15) translate(0,0)">
              <rect x="8" y="-22" width="24" height="32" rx="2" fill="#37474F" stroke="#546E7A" stroke-width="0.8"/>
              <rect x="9" y="-21" width="22" height="30" rx="1.5" fill="url(#panelGrad)"/>
              <!-- 电池片网格 -->
              <line x1="9" y1="-11" x2="31" y2="-11" stroke="#4A6AB0" stroke-width="0.4"/>
              <line x1="9" y1="-1" x2="31" y2="-1" stroke="#4A6AB0" stroke-width="0.4"/>
              <line x1="9" y1="9" x2="31" y2="9" stroke="#4A6AB0" stroke-width="0.4"/>
              <line x1="17" y1="-21" x2="17" y2="9" stroke="#4A6AB0" stroke-width="0.4"/>
              <rect x="9" y="-21" width="22" height="8" rx="1" fill="url(#panelShine)"/>
            </g>
            <!-- 面板2 -->
            <g transform="skewX(-15) translate(28,0)">
              <rect x="8" y="-22" width="24" height="32" rx="2" fill="#37474F" stroke="#546E7A" stroke-width="0.8"/>
              <rect x="9" y="-21" width="22" height="30" rx="1.5" fill="url(#panelGrad2)"/>
              <line x1="9" y1="-11" x2="31" y2="-11" stroke="#4A6AB0" stroke-width="0.4"/>
              <line x1="9" y1="-1" x2="31" y2="-1" stroke="#4A6AB0" stroke-width="0.4"/>
              <line x1="9" y1="9" x2="31" y2="9" stroke="#4A6AB0" stroke-width="0.4"/>
              <line x1="17" y1="-21" x2="17" y2="9" stroke="#4A6AB0" stroke-width="0.4"/>
              <rect x="9" y="-21" width="22" height="8" rx="1" fill="url(#panelShine)"/>
            </g>
            <!-- 面板3 -->
            <g transform="skewX(-15) translate(56,0)">
              <rect x="8" y="-22" width="24" height="32" rx="2" fill="#37474F" stroke="#546E7A" stroke-width="0.8"/>
              <rect x="9" y="-21" width="22" height="30" rx="1.5" fill="url(#panelGrad)"/>
              <line x1="9" y1="-11" x2="31" y2="-11" stroke="#4A6AB0" stroke-width="0.4"/>
              <line x1="9" y1="-1" x2="31" y2="-1" stroke="#4A6AB0" stroke-width="0.4"/>
              <line x1="9" y1="9" x2="31" y2="9" stroke="#4A6AB0" stroke-width="0.4"/>
              <line x1="17" y1="-21" x2="17" y2="9" stroke="#4A6AB0" stroke-width="0.4"/>
              <rect x="9" y="-21" width="22" height="8" rx="1" fill="url(#panelShine)"/>
            </g>
          </g>

          <!-- 阵列2: 前景右侧 (3块) -->
          <g transform="translate(310, 486)">
            <rect x="15" y="-2" width="3" height="42" rx="1" fill="#78909C"/>
            <rect x="72" y="-2" width="3" height="42" rx="1" fill="#78909C"/>
            <rect x="10" y="9" width="72" height="2.5" rx="1" fill="#90A4AE"/>
            <!-- 面板1 -->
            <g transform="skewX(-14) translate(0,0)">
              <rect x="6" y="-19" width="21" height="28" rx="2" fill="#37474F" stroke="#546E7A" stroke-width="0.7"/>
              <rect x="7" y="-18" width="19" height="26" rx="1.5" fill="url(#panelGrad2)"/>
              <line x1="7" y1="-9" x2="26" y2="-9" stroke="#4A6AB0" stroke-width="0.35"/>
              <line x1="7" y1="0" x2="26" y2="0" stroke="#4A6AB0" stroke-width="0.35"/>
              <line x1="7" y1="8" x2="26" y2="8" stroke="#4A6AB0" stroke-width="0.35"/>
              <line x1="13" y1="-18" x2="13" y2="8" stroke="#4A6AB0" stroke-width="0.35"/>
              <line x1="20" y1="-18" x2="20" y2="8" stroke="#4A6AB0" stroke-width="0.35"/>
              <rect x="7" y="-18" width="19" height="7" rx="1" fill="url(#panelShine)"/>
            </g>
            <!-- 面板2 -->
            <g transform="skewX(-14) translate(24,0)">
              <rect x="6" y="-19" width="21" height="28" rx="2" fill="#37474F" stroke="#546E7A" stroke-width="0.7"/>
              <rect x="7" y="-18" width="19" height="26" rx="1.5" fill="url(#panelGrad)"/>
              <line x1="7" y1="-9" x2="26" y2="-9" stroke="#4A6AB0" stroke-width="0.35"/>
              <line x1="7" y1="0" x2="26" y2="0" stroke="#4A6AB0" stroke-width="0.35"/>
              <line x1="7" y1="8" x2="26" y2="8" stroke="#4A6AB0" stroke-width="0.35"/>
              <line x1="13" y1="-18" x2="13" y2="8" stroke="#4A6AB0" stroke-width="0.35"/>
              <line x1="20" y1="-18" x2="20" y2="8" stroke="#4A6AB0" stroke-width="0.35"/>
              <rect x="7" y="-18" width="19" height="7" rx="1" fill="url(#panelShine)"/>
            </g>
            <!-- 面板3 -->
            <g transform="skewX(-14) translate(48,0)">
              <rect x="6" y="-19" width="21" height="28" rx="2" fill="#37474F" stroke="#546E7A" stroke-width="0.7"/>
              <rect x="7" y="-18" width="19" height="26" rx="1.5" fill="url(#panelGrad2)"/>
              <line x1="7" y1="-9" x2="26" y2="-9" stroke="#4A6AB0" stroke-width="0.35"/>
              <line x1="7" y1="0" x2="26" y2="0" stroke="#4A6AB0" stroke-width="0.35"/>
              <line x1="7" y1="8" x2="26" y2="8" stroke="#4A6AB0" stroke-width="0.35"/>
              <line x1="13" y1="-18" x2="13" y2="8" stroke="#4A6AB0" stroke-width="0.35"/>
              <line x1="20" y1="-18" x2="20" y2="8" stroke="#4A6AB0" stroke-width="0.35"/>
              <rect x="7" y="-18" width="19" height="7" rx="1" fill="url(#panelShine)"/>
            </g>
          </g>

          <!-- 阵列3: 中景阵列 (3块) -->
          <g transform="translate(415, 468)">
            <rect x="10" y="-1" width="3" height="34" rx="1" fill="#78909C"/>
            <rect x="58" y="-1" width="3" height="34" rx="1" fill="#78909C"/>
            <rect x="7" y="7" width="58" height="2" rx="1" fill="#90A4AE"/>
            <!-- 面板1 -->
            <g transform="skewX(-16) translate(0,0)">
              <rect x="5" y="-16" width="17" height="23" rx="1.5" fill="#37474F" stroke="#546E7A" stroke-width="0.6"/>
              <rect x="6" y="-15" width="15" height="21" rx="1" fill="url(#panelGrad)"/>
              <line x1="6" y1="-8" x2="21" y2="-8" stroke="#4A6AB0" stroke-width="0.3"/>
              <line x1="6" y1="-1" x2="21" y2="-1" stroke="#4A6AB0" stroke-width="0.3"/>
              <line x1="6" y1="6" x2="21" y2="6" stroke="#4A6AB0" stroke-width="0.3"/>
              <line x1="11" y1="-15" x2="11" y2="6" stroke="#4A6AB0" stroke-width="0.3"/>
              <line x1="16" y1="-15" x2="16" y2="6" stroke="#4A6AB0" stroke-width="0.3"/>
            </g>
            <!-- 面板2 -->
            <g transform="skewX(-16) translate(19,0)">
              <rect x="5" y="-16" width="17" height="23" rx="1.5" fill="#37474F" stroke="#546E7A" stroke-width="0.6"/>
              <rect x="6" y="-15" width="15" height="21" rx="1" fill="url(#panelGrad2)"/>
              <line x1="6" y1="-8" x2="21" y2="-8" stroke="#4A6AB0" stroke-width="0.3"/>
              <line x1="6" y1="-1" x2="21" y2="-1" stroke="#4A6AB0" stroke-width="0.3"/>
              <line x1="6" y1="6" x2="21" y2="6" stroke="#4A6AB0" stroke-width="0.3"/>
              <line x1="11" y1="-15" x2="11" y2="6" stroke="#4A6AB0" stroke-width="0.3"/>
              <line x1="16" y1="-15" x2="16" y2="6" stroke="#4A6AB0" stroke-width="0.3"/>
            </g>
            <!-- 面板3 -->
            <g transform="skewX(-16) translate(38,0)">
              <rect x="5" y="-16" width="17" height="23" rx="1.5" fill="#37474F" stroke="#546E7A" stroke-width="0.6"/>
              <rect x="6" y="-15" width="15" height="21" rx="1" fill="url(#panelGrad)"/>
              <line x1="6" y1="-8" x2="21" y2="-8" stroke="#4A6AB0" stroke-width="0.3"/>
              <line x1="6" y1="-1" x2="21" y2="-1" stroke="#4A6AB0" stroke-width="0.3"/>
              <line x1="6" y1="6" x2="21" y2="6" stroke="#4A6AB0" stroke-width="0.3"/>
              <line x1="11" y1="-15" x2="11" y2="6" stroke="#4A6AB0" stroke-width="0.3"/>
              <line x1="16" y1="-15" x2="16" y2="6" stroke="#4A6AB0" stroke-width="0.3"/>
            </g>
          </g>

          <!-- ============================================ -->
          <!-- ===== 灌木装饰 ===== -->
          <!-- ============================================ -->
          <g transform="translate(50, 490)">
            <ellipse cx="0" cy="0" rx="20" ry="13" fill="#4DAD68"/>
            <ellipse cx="-10" cy="-5" rx="14" ry="11" fill="#5CB878"/>
            <ellipse cx="12" cy="-4" rx="12" ry="9" fill="#3D9858"/>
          </g>
          <g transform="translate(565, 478)">
            <ellipse cx="0" cy="0" rx="18" ry="12" fill="#4DAD68"/>
            <ellipse cx="-9" cy="-4" rx="13" ry="10" fill="#5CB878"/>
            <ellipse cx="11" cy="-3" rx="11" ry="8" fill="#3D9858"/>
          </g>

          <!-- ============================================ -->
          <!-- ===== 飘落树叶 ===== -->
          <!-- ============================================ -->
          <g opacity="0.7">
            <animateTransform attributeName="transform" type="translate" values="0,0;20,25;40,50" dur="8s" repeatCount="indefinite"/>
            <path d="M270 170 Q275 162 280 170 Q275 178 270 170 Z" fill="url(#leafGrad)" transform="rotate(30 275 170)"/>
          </g>
          <g opacity="0.6">
            <animateTransform attributeName="transform" type="translate" values="0,0;-18,35;-36,70" dur="10s" repeatCount="indefinite"/>
            <path d="M360 140 Q364 133 368 140 Q364 147 360 140 Z" fill="#6ECB8A" transform="rotate(-25 364 140)"/>
          </g>
          <g opacity="0.55">
            <animateTransform attributeName="transform" type="translate" values="0,0;15,30;30,60" dur="9s" repeatCount="indefinite"/>
            <path d="M310 155 Q314 148 318 155 Q314 162 310 155 Z" fill="#7ED89A" transform="rotate(40 314 155)"/>
          </g>

          <!-- ============================================ -->
          <!-- ===== 前景草地 ===== -->
          <!-- ============================================ -->
          <rect x="0" y="510" width="600" height="110" fill="#4D9A60" opacity="0.75"/>
          <rect x="0" y="525" width="600" height="95" fill="#3D8A50"/>
        </svg>
      </div>

      <!-- 插画上的标语 -->
      <div class="illustration-text">
        <h2 class="slogan">守护绿水青山</h2>
        <p class="slogan-sub">共建美丽中国</p>
        <div class="slogan-line"></div>
        <div class="system-badge">
          <span class="badge-icon">🍃</span>
          <span class="badge-label">东软环保公众监督系统</span>
          <span class="badge-icon">🍃</span>
        </div>
        <p class="slogan-desc">
          用数据守护每一片蓝天、碧水、净土。
        </p>
      </div>
    </div>

    <!-- ==================== 右侧：登录表单面板 ==================== -->
    <div class="login-panel">
      <div class="login-card">
        <!-- Logo & 标题 -->
        <div class="card-header">
          <div class="logo-wrapper">
            <div class="logo-icon">
              <svg viewBox="0 0 48 48" fill="none" xmlns="http://www.w3.org/2000/svg">
                <rect x="4" y="4" width="40" height="40" rx="10" fill="url(#logoGrad)" />
                <path d="M14 30L22 18L30 24L36 14" stroke="#fff" stroke-width="3" stroke-linecap="round" stroke-linejoin="round"/>
                <circle cx="36" cy="14" r="3" fill="#fff"/>
                <defs>
                  <linearGradient id="logoGrad" x1="0" y1="0" x2="48" y2="48">
                    <stop offset="0%" stop-color="#43A047"/>
                    <stop offset="100%" stop-color="#1B5E20"/>
                  </linearGradient>
                </defs>
              </svg>
            </div>
          </div>
          <h1 class="system-title">NEPM 管理平台</h1>
          <p class="system-subtitle">National Environment Protection Management</p>
        </div>

        <!-- 登录表单 -->
        <el-form
          ref="formRef"
          :model="form"
          :rules="rules"
          label-position="top"
          size="large"
          class="login-form"
          @keyup.enter="handleLogin"
        >
          <el-form-item label="管理员编码" prop="adminCode">
            <el-input
              v-model="form.adminCode"
              placeholder="请输入管理员编码"
              :prefix-icon="User"
              clearable
              class="custom-input"
            />
          </el-form-item>
          <el-form-item label="登录密码" prop="password">
            <el-input
              v-model="form.password"
              type="password"
              placeholder="请输入密码"
              :prefix-icon="Lock"
              show-password
              class="custom-input"
            />
          </el-form-item>
          <el-form-item>
            <el-button
              type="primary"
              :loading="loading"
              class="login-btn"
              @click="handleLogin"
            >
              <span v-if="!loading">登 录</span>
            </el-button>
          </el-form-item>
        </el-form>

        <!-- 底部 -->
        <div class="card-footer">
          <span class="footer-text">NEP 管理系统 v1.0</span>
        </div>
      </div>

      <!-- 右侧底部版权 -->
      <p class="copyright">© 2025 NEPM. All rights reserved.</p>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { User, Lock } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { useUserStore } from '../store/user'
import { adminLogin } from '../api/admin'

const router = useRouter()
const userStore = useUserStore()

const formRef = ref(null)
const loading = ref(false)
const form = reactive({
  adminCode: '',
  password: '',
})
const rules = {
  adminCode: [{ required: true, message: '请输入管理员编码', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
}

async function handleLogin() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  loading.value = true
  try {
    const res = await adminLogin(form.adminCode, form.password)
    userStore.setUser(res.data)
    ElMessage.success('登录成功')
    router.push('/index')
  } catch {
    // 错误已由拦截器统一处理
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
/* ==================== 整体布局 ==================== */
.login-page {
  display: flex;
  width: 100vw;
  height: 100vh;
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Microsoft YaHei', Arial, sans-serif;
  overflow: hidden;
}

/* ==================== 左侧：插画面板 ==================== */
.illustration-panel {
  flex: 0 0 50%;
  position: relative;
  background: linear-gradient(180deg, #74B9FF 0%, #A8D8FF 25%, #D4ECFF 50%, #D8ECD8 75%, #C0DCC0 100%);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  overflow: hidden;
}

/* 光晕装饰 */
.glow {
  position: absolute;
  border-radius: 50%;
  pointer-events: none;
  filter: blur(60px);
}

.glow-1 {
  width: 300px;
  height: 300px;
  background: rgba(255, 249, 196, 0.22);
  top: 3%;
  right: 8%;
  animation: glowPulse 5s ease-in-out infinite;
}

.glow-2 {
  width: 250px;
  height: 250px;
  background: rgba(100, 200, 140, 0.18);
  bottom: 12%;
  left: 3%;
  animation: glowPulse 6s ease-in-out infinite reverse;
}

.glow-3 {
  width: 200px;
  height: 200px;
  background: rgba(90, 170, 210, 0.16);
  top: 45%;
  left: 40%;
  animation: glowPulse 7s ease-in-out infinite;
}

@keyframes glowPulse {
  0%, 100% { opacity: 0.5; transform: scale(1); }
  50% { opacity: 0.9; transform: scale(1.12); }
}

/* 插画容器 */
.illustration-wrapper {
  width: 86%;
  max-width: 520px;
  position: relative;
  z-index: 1;
  margin-top: -10px;
}

.illustration-svg {
  width: 100%;
  height: auto;
  display: block;
  filter: drop-shadow(0 6px 20px rgba(0, 0, 0, 0.06));
}

/* 插画文字 */
.illustration-text {
  position: relative;
  z-index: 1;
  text-align: center;
  margin-top: 16px;
}

.slogan {
  margin: 0;
  font-size: 26px;
  font-weight: 800;
  background: linear-gradient(135deg, #1B5E20, #2E7D32, #43A047);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  letter-spacing: 4px;
}

.slogan-sub {
  margin: 4px 0 0;
  font-size: 14px;
  font-weight: 500;
  color: #4CAF50;
  letter-spacing: 8px;
}

.slogan-line {
  width: 50px;
  height: 3px;
  margin: 14px auto;
  background: linear-gradient(90deg, transparent, #4CAF50, transparent);
  border-radius: 2px;
}

/* 系统名称徽章 */
.system-badge {
  display: inline-flex;
  align-items: center;
  gap: 10px;
  margin: 6px auto 14px;
  padding: 12px 28px;
  background: linear-gradient(135deg, #1B5E20 0%, #2E7D32 40%, #1B5E20 100%);
  border: 2px solid rgba(255, 255, 255, 0.25);
  border-radius: 26px;
  box-shadow:
    0 0 0 4px rgba(27, 94, 32, 0.15),
    0 6px 24px rgba(27, 94, 32, 0.35),
    inset 0 1px 0 rgba(255, 255, 255, 0.15);
  animation: badgePulse 2.2s ease-in-out infinite;
  position: relative;
  overflow: hidden;
}

/* 徽章光泽扫过 */
.system-badge::after {
  content: '';
  position: absolute;
  top: -50%;
  left: -80%;
  width: 60%;
  height: 200%;
  background: linear-gradient(
    90deg,
    transparent,
    rgba(255, 255, 255, 0.2),
    transparent
  );
  transform: skewX(-25deg);
  animation: badgeShine 3s ease-in-out infinite;
}

@keyframes badgeShine {
  0%, 100% { left: -80%; }
  50% { left: 120%; }
}

@keyframes badgePulse {
  0%, 100% {
    box-shadow:
      0 0 0 4px rgba(27, 94, 32, 0.15),
      0 6px 24px rgba(27, 94, 32, 0.35),
      inset 0 1px 0 rgba(255, 255, 255, 0.15);
    transform: scale(1);
  }
  50% {
    box-shadow:
      0 0 0 8px rgba(27, 94, 32, 0.1),
      0 10px 36px rgba(27, 94, 32, 0.5),
      inset 0 1px 0 rgba(255, 255, 255, 0.2);
    transform: scale(1.04);
  }
}

.badge-label {
  font-size: 17px;
  font-weight: 800;
  color: #fff;
  letter-spacing: 4px;
  white-space: nowrap;
  text-shadow: 0 1px 3px rgba(0, 0, 0, 0.2);
}

.badge-icon {
  font-size: 18px;
  line-height: 1;
  animation: leafSway 2s ease-in-out infinite;
}

.badge-icon:last-child {
  animation-delay: 0.5s;
}

@keyframes leafSway {
  0%, 100% { transform: rotate(0deg) scale(1); }
  25% { transform: rotate(10deg) scale(1.1); }
  75% { transform: rotate(-8deg) scale(1.05); }
}

.slogan-desc {
  margin: 0;
  font-size: 12.5px;
  color: #5E8B6A;
  line-height: 1.8;
  letter-spacing: 1px;
}

/* ==================== 右侧：登录面板 ==================== */
.login-panel {
  flex: 0 0 50%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: #f8f9fb;
  position: relative;
}

.login-card {
  width: 420px;
  padding: 44px 44px 34px;
  background: #fff;
  border-radius: 20px;
  box-shadow:
    0 2px 4px rgba(0, 0, 0, 0.04),
    0 8px 24px rgba(0, 0, 0, 0.06),
    0 24px 56px rgba(0, 0, 0, 0.08);
  animation: cardIn 0.7s cubic-bezier(0.16, 1, 0.3, 1);
}

@keyframes cardIn {
  0% {
    opacity: 0;
    transform: translateX(40px) scale(0.97);
  }
  100% {
    opacity: 1;
    transform: translateX(0) scale(1);
  }
}

/* ==================== 卡片头部 ==================== */
.card-header {
  text-align: center;
  margin-bottom: 34px;
}

.logo-wrapper {
  margin-bottom: 16px;
}

.logo-icon {
  width: 56px;
  height: 56px;
  margin: 0 auto;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 6px 20px rgba(67, 160, 71, 0.3);
}

.logo-icon svg {
  width: 100%;
  height: 100%;
  display: block;
}

.system-title {
  margin: 0;
  font-size: 24px;
  font-weight: 700;
  color: #1a1a2e;
  letter-spacing: 2px;
}

.system-subtitle {
  margin: 6px 0 0;
  font-size: 11px;
  color: #aaa;
  letter-spacing: 1.2px;
  text-transform: uppercase;
}

/* ==================== 表单样式 ==================== */
.login-form {
  margin-top: 4px;
}

.login-form :deep(.el-form-item) {
  margin-bottom: 22px;
}

.login-form :deep(.el-form-item__label) {
  font-weight: 500;
  color: #555;
  font-size: 14px;
  padding-bottom: 6px;
}

.custom-input :deep(.el-input__wrapper) {
  border-radius: 10px;
  border: 1.5px solid #e8e8e8;
  background: #fafbfc;
  transition: all 0.3s ease;
  box-shadow: none;
}

.custom-input :deep(.el-input__wrapper:hover) {
  border-color: #c8d6c0;
  background: #fff;
}

.custom-input :deep(.el-input__wrapper.is-focus) {
  border-color: #43A047;
  background: #fff;
  box-shadow: 0 0 0 3px rgba(67, 160, 71, 0.1);
}

.custom-input :deep(.el-input__prefix) {
  color: #b8c8b0;
  transition: color 0.3s;
}

.custom-input :deep(.el-input__wrapper.is-focus .el-input__prefix) {
  color: #43A047;
}

.custom-input :deep(.el-input__clear) {
  color: #ccc;
}

/* ==================== 登录按钮 ==================== */
.login-btn {
  width: 100%;
  height: 48px;
  margin-top: 6px;
  font-size: 16px;
  font-weight: 600;
  letter-spacing: 6px;
  border: none;
  border-radius: 12px;
  background: linear-gradient(135deg, #43A047 0%, #2E7D32 50%, #1B5E20 100%);
  transition: all 0.4s cubic-bezier(0.16, 1, 0.3, 1);
  position: relative;
  overflow: hidden;
}

.login-btn::before {
  content: '';
  position: absolute;
  inset: 0;
  background: linear-gradient(135deg, #1B5E20 0%, #43A047 100%);
  opacity: 0;
  transition: opacity 0.4s;
  border-radius: 12px;
}

.login-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 28px rgba(67, 160, 71, 0.35);
}

.login-btn:hover::before {
  opacity: 1;
}

.login-btn:active {
  transform: translateY(0);
  box-shadow: 0 4px 12px rgba(67, 160, 71, 0.25);
}

.login-btn span {
  position: relative;
  z-index: 1;
}

.login-btn.is-loading {
  background: linear-gradient(135deg, #43A047 0%, #2E7D32 50%, #1B5E20 100%);
}

/* ==================== 卡片底部 ==================== */
.card-footer {
  text-align: center;
  margin-top: 26px;
  padding-top: 18px;
  border-top: 1px solid #f0f0f0;
}

.footer-text {
  font-size: 12px;
  color: #ccc;
  letter-spacing: 1px;
}

/* 版权信息 */
.copyright {
  margin: 20px 0 0;
  font-size: 12px;
  color: #bbb;
  letter-spacing: 0.5px;
}

/* ==================== 响应式 ==================== */
@media (max-width: 900px) {
  .illustration-panel {
    display: none;
  }

  .login-panel {
    flex: 1 1 100%;
    padding: 20px;
  }

  .login-card {
    width: 100%;
    max-width: 400px;
  }
}
</style>
