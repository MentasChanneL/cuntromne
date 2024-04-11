package org.pinusgames.cuntromne.weapon.script;

import java.util.HashMap;

public class Animations {
    public static final HashMap<String, int[]> animations = new HashMap<>();
    public static void initAnimates() {
        animations.put("ak.fire", new int[]{2, 3, 1});
        animations.put("ak.intro", new int[]{
                54, 55, 55, 56, 56, 57, 57, 58, 58, 59, 59, 60, 60, 61, 61,
                62, 62, 63, 63, 64, 64, 65, 65, 66, 66, 67, 67, 68, 69, 70, 1});
        animations.put("ak.reload", new int[]{
                10, 10, 11, 11, 12, 12, 13, 13, 14, 14, 15, 15, 16, 16, 17,
                17, 18, 18, 19, 19, 20, 20, 21, 21, 22, 22, 23, 23, 24, 24,
                25, 25, 26, 26, 27, 27, 28, 40, 40, 41, 41, 42, 42, 43, 43,
                44, 44, 45, 45, 28, 29, 29, 30, 30, 31, 31, 32, 32, 33, 33, 34, 34, 35, 35, 36, 36, 37, 37, 1});
        animations.put("ak.review.1", new int[]{
                80, 82, 84, 86, 85, 83, 81,
                80, 82, 84, 86, 85, 83, 81,
                80, 82, 84, 86, 85, 83, 81, 80, 1
        });
        animations.put("ak.review.2", new int[]{
                1, 1, 1, 1, 1, 1, 87, 87, 87, 87, 87, 87, 87, 87, 1
        });
        animations.put("deagle.fire", new int[]{
                100, 101, 102, 100
        });
        animations.put("deagle.reload", new int[]{
                110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120,
                121, 122, 123, 124, 125, 126, 127, 128, 129, 130, 131,
                132, 133, 134, 135, 136, 137, 138, 139, 100
        });
        animations.put("deagle.intro", new int[]{
                150, 151, 152, 153, 154, 155, 156, 157, 158, 159
        });
        animations.put("m4a1.fire", new int[]{
                200, 201, 202, 200
        });
        animations.put("m4a1.reload", new int[]{
                210, 211, 212, 213, 214, 215, 216, 217, 218, 219, 220,
                221, 222, 223, 224, 225, 226, 227, 228, 229, 230, 231,
                232, 233, 234, 200
        });
        animations.put("m4a1.intro", new int[]{
                250, 251, 252, 253, 254, 255, 256, 257, 258, 259, 260,
                261, 262, 263, 264, 265, 200
        });
        animations.put("awp.fire", new int[]{
                301, 302, 300, 300, 300, 300, 300, 320, 321, 322, 323, 324, 325,
                326, 327, 328, 329, 330, 331, 332, 333, 334, 335, 336, 337, 300
        });
        animations.put("awp.intro", new int[]{
                310, 311, 312, 313, 314, 315, 320, 321, 322, 323, 324, 325,
                326, 327, 328, 329, 330, 331, 332, 333, 334, 335, 336, 337, 300
        });
        animations.put("awp.reload", new int[]{
                350, 351, 352, 353, 354, 355, 356, 357, 358, 359,
                360, 361, 362, 363, 364, 365, 366, 300, 320, 321, 322, 323, 324, 325,
                326, 327, 328, 329, 330, 331, 332, 333, 334, 335, 336, 337, 300
        });
        animations.put("glock.fire", new int[]{
                401, 402, 403, 400
        });
        animations.put("glock.reload", new int[]{
                400, 410, 411, 412, 413, 414, 415, 416, 417,
                418, 419, 420, 421, 422, 423, 424, 425, 426,
                427, 428, 429, 430, 431, 432, 433, 434, 435, 400
        });
        animations.put("glock.intro", new int[]{
                450, 451, 452, 453, 454, 455, 456, 457, 458, 459,
                460, 461, 462, 463, 464, 465, 466, 400
        });
        animations.put("usp.fire", new int[]{
                501, 502, 503, 500
        });
        animations.put("usp.reload", new int[]{
                510, 511, 512, 513, 514, 515, 516, 517, 518, 520, 521, 522, 523, 524, 525,
                526, 527, 528, 529, 530, 531, 532, 533, 534, 535, 536, 537
        });
        animations.put("usp.intro", new int[]{
                550, 551, 552, 553, 554, 555, 556, 557, 558, 559, 560, 561, 562, 563, 564,
                565, 566, 567, 568, 500
        });
    }
}
