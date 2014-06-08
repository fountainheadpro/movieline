  /** @jsx React.DOM */

  var SearchBox = React.createClass({
    getInitialState: function() {
      return {
        prompt: "Zed's dead...",
        query: ""
      };
    },    

    handleChange: function() {
        //this.props.onUserInput(
        //   this.refs.filterTextInput.getDOMNode().value,            
        //);
    },    

    render: function() {
        return (
            <form>
              <div className="searchContainer" >
                <input 
                  type="text" 
                  className="searchBar" 
                  placeholder={this.state.prompt} 
                  ref="filterTextInput"
                  value={this.props.query} />
                <span className="glyphicon glyphicon-search searchButton"></span>
              </div>  
            </form>
        );
    }
  });


var Scene=React.createClass({
  render: function(){
    return (
          <div className="col-sm-6 col-md-6">
            <div className="thumbnail" onClick={this.handleClick}>      
              <img src={this.props.scene.imageSource} alt={this.props.scene.caption}/>                          
              <blockquote>
                <p>{this.props.scene.caption}</p>
              </blockquote>
            </div>
          </div>                   
    )    
  },

  handleClick: function(event) {
    
  },

});  

/** @jsx React.DOM */
var Clip = React.createClass({
    render: function() {
      ///var scope = this.props.scope;
      var images = [];
      this.props.scope.data.forEach(function(scene){
        images.push(
              <Scene scene={scene} key={scene.key}/>
            )
      }.bind(this));
      return (
          <div className="thumbnails">
            {images}
          </div>
      )      
    }
});