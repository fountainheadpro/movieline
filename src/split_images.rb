require 'phashion'

MAX_SCENE_RADIUS=20

def process_block(i)
  images=Dir["./tmp/#{i}/*.png"].map{|file| Phashion::Image.new file}

  scenes=[]

  images.zip(images.drop(1)){|img1, img2|
   neighbours_distance= img2.nil? ? 0 : img1.distance_from(img2)
   if scenes.last.nil?
     scenes<<{number: scenes.length+1, files: [img1.filename], radius: 0}
   else
     if neighbours_distance<MAX_SCENE_RADIUS
      scenes.last[:radius]=[scenes.last[:radius],neighbours_distance].max
      scenes.last[:files] << img1.filename
     else
       scenes.last[:files] << img1.filename
       scenes<<{number: scenes.length+1, files: [], radius: 0}
     end
   end
  }

  scenes.each{|scene|
    `convert -delay 1x10 #{scene[:files].join(' ')} \
              -coalesce -layers OptimizeTransparency \
              +map ./animations/animation_#{i}_#{scene[:number]}.gif`
  }

end



process_block(ARGV[0])


