#version 120

varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform float time;
uniform float px;
uniform float py;

void main() {
  gl_FragColor = texture2D(u_texture, v_texCoords);
    float gray = (gl_FragColor.r + gl_FragColor.g + gl_FragColor.b) / 3.0f;

    float dst = sqrt((v_texCoords.x - px) * (v_texCoords.x - px)
                + (v_texCoords.y - py) * (v_texCoords.y - py));

        gl_FragColor.r *= (1.7f - dst * 1.5f);
        gl_FragColor.g *= (1.7f - dst * 1.5f) ;
        gl_FragColor.b *= (1.7f - dst * 1.5f) ;




  //float dst = sqrt((v_texCoords.x - px) * (v_texCoords.x - px)
  //              + (v_texCoords.y - py) * (v_texCoords.y - py));
  //float gray = (gl_FragColor.r + gl_FragColor.g + gl_FragColor.b) / 3.0f;
//
  //    gl_FragColor.r *= (1.8f - dst * 2);
  //    gl_FragColor.g *= (1.8f - dst * 2) ;
  //    gl_FragColor.b *= (1.8f - dst * 2) ;




//  float gray = (gl_FragColor.r + gl_FragColor.g + gl_FragColor.b) / 3.0f;
//  gl_FragColor.r = gray;
//  gl_FragColor.g = gray;
//  gl_FragColor.b = gray;

//  float gray = (gl_FragColor.r + gl_FragColor.g + gl_FragColor.b) / 3.0f;
//  float dst = sqrt((v_texCoords.x - px) * (v_texCoords.x - px) + (v_texCoords.y - py) * (v_texCoords.y - py));
//  gl_FragColor.r *= (2.0f - dst * 3);
//  gl_FragColor.g *= (2.0f - dst * 3);
//  gl_FragColor.b *= (2.0f - dst * 3);


}
